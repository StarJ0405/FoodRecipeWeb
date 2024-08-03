package com.StarJ.food_recipe.Entities.PredictData;

import com.StarJ.food_recipe.Entities.Configs.Config;
import com.StarJ.food_recipe.Entities.Configs.ConfigService;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEvalService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserService;
import com.StarJ.food_recipe.FoodRecipeApplication;
import com.StarJ.food_recipe.Global.OSType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictDatumService {
    private final RecipeService recipeService;
    private final UserService userService;
    private final RecipeEvalService recipeEvalService;
    private final PredictDatumRepository predictDatumRepository;
    private final ConfigService configService;
    private boolean status = false;

    public void reset(){predictDatumRepository.deleteAll();}
    public List<PredictDatum> getTop10() {
        return predictDatumRepository.getTop10();
    }

    public List<PredictDatum> getTop5(SiteUser user) {
        return predictDatumRepository.getTop5(user);
    }

    private final String newLine = System.lineSeparator();


    private static BufferedWriter getBufferedWriter(String loc, String file_loc) throws IOException {
        File folder = new File(FoodRecipeApplication.getOS_TYPE().getPath() + "/" + loc);
        String definePath = FoodRecipeApplication.getOS_TYPE().getPath() + "/" + loc + "/" + file_loc;
        if (!folder.exists())
            folder.mkdirs();
        File file = new File(definePath);
        if (!file.exists())
            file.createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        return bufferedWriter;
    }

    private void writeDefine(Config config) throws IOException {
        System.out.println("Data Defining Start");
        BufferedWriter defineBW = getBufferedWriter("database", "defined.csv");
        if (config == null || config.getIntegerValue() == null)
            for (RecipeEval eval : recipeEvalService.getEvalsByLimited()) {
                defineBW.write(eval.getSiteUser().getId() + "," + eval.getRecipe().getId() + "," + eval.getVal() + "," + Long.parseLong(eval.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
                defineBW.write(newLine);
            }
        else {
            Integer start = config.getIntegerValue();
            System.out.println("continue write : " + start.toString());
            for (RecipeEval eval : recipeEvalService.getEvalsByLimited(start)) {
                defineBW.write(eval.getSiteUser().getId() + "," + eval.getRecipe().getId() + "," + eval.getVal() + "," + Long.parseLong(eval.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
                defineBW.write(newLine);
            }
        }
        defineBW.flush();
        defineBW.close();
        System.out.println("Data Defining End");
    }

    private void writeUnseen() throws IOException {
        System.out.println("Unseen List Loading Start");
        //"C:/Users/admin/IdeaProjects/FoodRecipeWeb/out/database/unseen.csv";
        BufferedWriter unseenBW = getBufferedWriter("database", "unseen.csv");
        for (String user : userService.getUsersId()) {
            for (Integer recipe : recipeService.getUnseenRecipe(user)) {
                unseenBW.write(user + "," + recipe);
                unseenBW.write(newLine);
            }
            unseenBW.flush();
        }
        unseenBW.close();
        System.out.println("Unseen List Loading End");
    }

    private final ResourceLoader resourceLoader;

    private void copyModelFile(OSType osType) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/static/define_model.py");
        if (resource.exists()) {
            InputStream inputStream = resource.getInputStream();
            File file = new File(osType.getPath() + "/database/define_model.py");
            file.getParentFile().mkdirs();
            try (OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else
            System.out.println("모델 파일이 존재하지않습니다.");
    }

    @Async
    public void training() {
        // 데이터 개수 확인 > 일정 이상이면 > 아니면 return;
        try {
            Config config = configService.getData("re.last");
            Integer last = recipeEvalService.getLastEvalID(config);
//            int N = 500;
            if (status) {
                System.out.println("Already Study is Processing.");
                return;
            }
//            if (last == null  // 쌓인 데이터가 0인 경우
//                    || ((config == null || config.getIntegerValue() == null) && last < N) // 마지막 기록이 0, 0 ~ last까지가 N개 미만인 경우
//                    || (config != null && config.getIntegerValue() != null && last - config.getIntegerValue() < N)) // 마지막 기록 ~ last 까지가 N개 미만인 경우
//            {
//                System.out.println("Need More data trying to study");
//                return;
//            }
            status = true;
            writeDefine(config); // 평점 저장
            writeUnseen(); // 미시청 데이터 저장
            configService.<Integer>setData(config, last);
            OSType osType = FoodRecipeApplication.getOS_TYPE();
            copyModelFile(osType);
            System.out.println(osType.getPython() + " " + osType.getPath() + "/database/define_model.py " + osType.getPath() + "/database");
            ProcessBuilder processBuilder = new ProcessBuilder(osType.getPython(), osType.getPath() + "/database/define_model.py", osType.getPath() + "/database");
//                    new ProcessBuilder(osType.getPython(), "./define_model.py", osType.getPath() + "/database");
            System.out.println("training Start");
            Process process = processBuilder.start();

            System.out.println("input_reader start");
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            if (reader != null || !reader.equals(""))
                while ((line = reader.readLine()) != null) {
                    // 실행 결과 처리
                    if (line.contains(" ")) {
                        String[] sp = line.split(" ");
                        if (sp.length >= 3) try {
                            String userID = sp[0];
                            SiteUser user = userService.getUserbyID(sp[0]);
                            Integer itemID = Integer.parseInt(sp[1]);
                            Recipe recipe = recipeService.getRecipe(itemID);
                            Double val = Double.parseDouble(sp[2]);
                            PredictDatum predictDatum = PredictDatum.builder().user(user).recipe(recipe).predict_val(val).build();
                            predictDatumRepository.save(predictDatum);
//                        System.out.println("전달 받은 데이터 : " + userID + ", " + itemID + ", " + val);
                        } catch (Exception exception) {
                            System.out.println("line error");
                        }
                    }
                }
            System.out.println("input_reader end");
            InputStream errorStream = process.getErrorStream();
            System.out.println("error_reader start");
            BufferedReader error_reader = new BufferedReader((new InputStreamReader((errorStream))));
            String error_line;
            if (error_reader != null || !error_reader.equals(""))
                while ((error_line = error_reader.readLine()) != null)
                    System.out.println(error_line);
            System.out.println("error_reader end");

            System.out.println("training end(" + process.waitFor() + ")");
            status = false;
//            training();
        } catch (IOException | InterruptedException e) {
            System.out.println("training error");
            throw new RuntimeException(e);
        }
    }

    public Long getCount() {
        return predictDatumRepository.getCount();
    }
}

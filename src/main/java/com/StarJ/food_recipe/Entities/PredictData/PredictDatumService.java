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
        System.out.println("데이터 정제 시작");
        BufferedWriter defineBW = getBufferedWriter("database", "defined.csv");
        if (config == null || config.getIntegerValue() == null)
            for (RecipeEval eval : recipeEvalService.getEvals()) {
                defineBW.write(eval.getSiteUser().getId() + "," + eval.getRecipe().getId() + "," + eval.getVal() + "," + Long.parseLong(eval.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
                defineBW.write(newLine);
            }
        else {
            Integer start = config.getIntegerValue();
            System.out.println("continue write : " + start.toString());
            for (RecipeEval eval : recipeEvalService.getEvals(start)) {
                defineBW.write(eval.getSiteUser().getId() + "," + eval.getRecipe().getId() + "," + eval.getVal() + "," + Long.parseLong(eval.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
                defineBW.write(newLine);
            }
        }
        defineBW.flush();
        defineBW.close();
        System.out.println("데이터 정제 끝");
    }

    private void writeUnseen() throws IOException {
        System.out.println("안 읽은 목록 로딩 시작");
        //"C:/Users/admin/IdeaProjects/FoodRecipeWeb/out/database/unseen.csv";
        BufferedWriter unseenBW = getBufferedWriter("database", "unseen.csv");
        for (SiteUser user : userService.getUsers())
            for (Recipe recipe : recipeService.getUnseenRecipe(user)) {
                unseenBW.write(user.getId() + "," + recipe.getId());
                unseenBW.write(newLine);
            }
        unseenBW.flush();
        unseenBW.flush();
        System.out.println("안 읽은 목록 로딩 끝");
    }

    @Async
    public void training() {
        // 데이터 개수 확인 > 일정 이상이면 > 아니면 return;
        try {
            Config config = configService.getData("re.last");
            Integer last = recipeEvalService.getLastEvalID();
            int N = 100;
            if (last == null  // 쌓인 데이터가 0인 경우
                    || ((config == null || config.getIntegerValue() == null) && last < N) // 마지막 기록이 0, 0~last까지가 N개 미만인 경우
                    || (config != null && config.getIntegerValue() != null && last - config.getIntegerValue() < N)) // 마지막 기록 ~ last 까지가 N개 미만인 경우
            {
                System.out.println("데이터 개수 부족으로 학습시도를 종료합니다.");
                return;
            }
            writeDefine(config); // 평점 저장
            writeUnseen(); // 미시청 데이터 저장
            configService.<Integer>setData(config, last);
            OSType osType = FoodRecipeApplication.getOS_TYPE();
            System.out.println("training start\n"+osType.getPython()+" ./define_model.py "+osType.getPath() + "/database");

            ProcessBuilder processBuilder =         new ProcessBuilder("python", "./define_model.py","C:/web/database");
//                    new ProcessBuilder(osType.getPython(), "./define_model.py", osType.getPath() + "/database");

            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // 실행 결과 처리
                if (line.contains(" ")) {
                    String[] sp = line.split(" ");
                    if (sp.length == 3) try {
                        String userID = sp[0];
                        SiteUser user = userService.getUserbyID(sp[0]);
                        Integer itemID = Integer.parseInt(sp[1]);
                        Recipe recipe = recipeService.getRecipe(itemID);
                        Double val = Double.parseDouble(sp[2]);
                        PredictDatum predictDatum = PredictDatum.builder().user(user).recipe(recipe).predict_val(val).build();
                        predictDatumRepository.save(predictDatum);
//                        System.out.xprintln("전달 받은 데이터 : " + userID + ", " + itemID + ", " + val);
                    } catch (Exception exception) {
                        System.out.println("line error");
                    }
                }
            }
            System.out.println("training end(" + process.waitFor() + ")");
        } catch (IOException | InterruptedException e) {
            System.out.println("training error");
            throw new RuntimeException(e);
        }
    }
}

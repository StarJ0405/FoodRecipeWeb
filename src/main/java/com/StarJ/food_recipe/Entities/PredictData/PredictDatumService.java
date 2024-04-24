package com.StarJ.food_recipe.Entities.PredictData;

import com.StarJ.food_recipe.Entities.Configs.Config;
import com.StarJ.food_recipe.Entities.Configs.ConfigService;
import com.StarJ.food_recipe.Entities.Recipes.Recipe;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEval;
import com.StarJ.food_recipe.Entities.Recipes.RecipeEvals.RecipeEvalService;
import com.StarJ.food_recipe.Entities.Recipes.RecipeService;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserService;
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

    public List<PredictDatum> getTop5(SiteUser user) {
        return predictDatumRepository.getTop5(user);
    }

    private final String newLine = System.lineSeparator();

    private Integer getLastId() {
        return null;
    }

    private void writeDefine() throws IOException {
        String definePath = "C:/Users/admin/IdeaProjects/FoodRecipeWeb/out/database/defined.csv";
        File defineFile = new File(definePath);
        BufferedWriter defineBW = new BufferedWriter(new FileWriter(defineFile));
        Config config = configService.getData("re.last");
        if (config == null || config.getIntegerValue() == null)
            for (RecipeEval eval : recipeEvalService.getEvals()) {
                defineBW.write(eval.getSiteUser().getId() + "," + eval.getRecipe().getId() + "," + eval.getVal() + "," + Long.parseLong(eval.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
                defineBW.write(newLine);
            }
        else {
            Integer start = config.getIntegerValue();
            for (RecipeEval eval : recipeEvalService.getEvals(start)) {
                defineBW.write(eval.getSiteUser().getId() + "," + eval.getRecipe().getId() + "," + eval.getVal() + "," + Long.parseLong(eval.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))));
                defineBW.write(newLine);
            }
        }
        configService.setData(config, recipeEvalService.getLastEvalID());
        defineBW.flush();
        defineBW.close();

    }

    private void writeUnseen() throws IOException {
        String unseenPath = "C:/Users/admin/IdeaProjects/FoodRecipeWeb/out/database/unseen.csv";
        File unseenFile = new File(unseenPath);
        BufferedWriter unseenBW = new BufferedWriter(new FileWriter(unseenFile));
        for (SiteUser user : userService.getUsers())
            for (Recipe recipe : recipeService.getUnseenRecipe(user)) {
                unseenBW.write(user.getId() + "," + recipe.getId());
                unseenBW.write(newLine);
            }
        unseenBW.flush();
        unseenBW.flush();
    }

    @Async
    public void training() {
        try {
            writeDefine(); // 평점 저장
            writeUnseen(); // 미시청 데이터 저장
            System.out.println("training start");
            ProcessBuilder processBuilder = new ProcessBuilder("python", "define_model.py").directory(new File("./out/database"));
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

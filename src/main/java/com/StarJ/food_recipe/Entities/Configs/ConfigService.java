package com.StarJ.food_recipe.Entities.Configs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigService {
    private final ConfigRepository configRepository;

    public <T> void setData(Config config, T data) {
        if (data == null)
            configRepository.delete(config);
        else {
            String value = data.toString();
            if (data instanceof String)
                config.setType("String");
            else if (data instanceof Integer)
                config.setType("Integer");
            config.setValue(value);
            configRepository.save(config);
        }
    }

    public Config getData(String key) {
        Optional<Config> _config = configRepository.findById(key);
        if (_config.isPresent())
            return _config.get();
        else
            return configRepository.save(Config.builder().key(key).build());
    }

}

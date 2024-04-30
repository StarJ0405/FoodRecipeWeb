package com.StarJ.food_recipe.Entities.Configs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigService {
    private final ConfigRepository configRepository;

    public void reset() {
        configRepository.deleteAll();
    }

    public <T> void setData(String config, T data) {
        setData(getData(config), data);
    }

    public <T> void setData(Config config, T data) {
        if (data == null)
            configRepository.delete(config);
        else {
            String value = data.toString();
            if (data instanceof String)
                config.setType("String");
            else if (data instanceof Integer)
                config.setType("Integer");
            else if (data instanceof Boolean)
                config.setType("Boolean");
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

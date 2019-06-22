package org.toughsocks.component;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.toughsocks.common.CoderUtil;
import org.toughsocks.common.ValidateUtil;
import org.toughsocks.config.Constant;
import org.toughsocks.entity.Config;
import org.toughsocks.mapper.ConfigMapper;

import java.util.List;

@Service
public class ConfigService implements Constant {

    @Autowired
    private ConfigMapper configMapper;

    public Config findConfig(String module, String name){
        return configMapper.findConfig(module,name);
    }

    public String getStringValue(String module, String name){
        Config cfg = configMapper.findConfig(module,name);
        if(cfg!=null){
            return cfg.getValue();
        }
        return null;
    }

    public String getStringValue(String module, String name, String defval){
        Config cfg = configMapper.findConfig(module,name);
        if(cfg!=null){
            String value =  cfg.getValue();
            return ValidateUtil.isEmpty(value)?defval:value;
        }
        return defval;
    }

    public void insertConfig(Config config){
        configMapper.insertConfig(config);
    }

    public void updateConfig(Config config){
        Config cfg = configMapper.findConfig(config.getType(),config.getName());
        if(cfg==null){
            config.setId(CoderUtil.randomLongId());
            configMapper.insertConfig(config);
        }else{
            configMapper.updateConfig(config);
        }
    }

    public void deleteById(Integer id){
        configMapper.deleteById(id);
    }

    public void deleteConfig(@Param(value = "type") String type, @Param(value = "name") String name){
        configMapper.deleteConfig(type,name);
    }

    public List<Config> queryForList(@Param(value = "type") String type){
        return configMapper.queryForList(type);
    }

}

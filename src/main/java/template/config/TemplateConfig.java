package template.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import template.application.domain.model.ItemId;

@Configuration
public class TemplateConfig {

    @Bean
    public ModelMapper modelMapper() {
        var mapper = new ModelMapper();

        mapper.addConverter(context -> context.getSource() == null ? null : new ItemId(context.getSource()), String.class, ItemId.class);
        mapper.addConverter(context -> context.getSource() == null ? null : context.getSource().value(), ItemId.class, String.class);

        return mapper;
    }

}

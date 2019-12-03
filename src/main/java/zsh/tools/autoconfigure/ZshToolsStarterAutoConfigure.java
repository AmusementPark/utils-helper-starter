package zsh.tools.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zsh.tools.aop.TimeConsumeAspect;

@Configuration
public class ZshToolsStarterAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(TimeConsumeAspect.class)
    @ConditionalOnClass({
        org.slf4j.Logger.class,
        org.slf4j.LoggerFactory.class
    })
    public TimeConsumeAspect timeConsumeAspectBean() {
        return new TimeConsumeAspect();
    }
}

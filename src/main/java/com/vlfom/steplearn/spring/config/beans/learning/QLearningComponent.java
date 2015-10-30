package com.vlfom.steplearn.spring.config.beans.learning;

import com.vlfom.steplearn.core.learning.general.MarkovDecisionProcess;
import com.vlfom.steplearn.core.learning.general.QLearning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
public class QLearningComponent {

    @Bean
    @Autowired
    public QLearning qLearning(MarkovDecisionProcess markovDecisionProcess) {
        return new QLearning(markovDecisionProcess);
    }
}

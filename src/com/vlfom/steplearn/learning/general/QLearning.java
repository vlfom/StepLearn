package com.vlfom.steplearn.learning.general;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public abstract class QLearning {
    private MarkovDecisionProcess mdp;
    TreeMap <Long, TreeMap <Long, Double> > q;
    Random random;

    public QLearning(MarkovDecisionProcess mdp) {
        this.mdp = mdp;
        q = new TreeMap<>();
        random = new Random();
    }

    public void update(State s, Action a, State n) {
        long sHash = s.hash();
        if(!q.containsKey(sHash)) {
            q.put(sHash, new TreeMap<Long, Double>());
        }
        TreeMap<Long, Double> actions = q.get(sHash);
        Long aHash = a.hash();
        Double prev = 0.0;
        if(actions.containsKey(aHash))
            prev = actions.get(sHash);
        actions.put(sHash, (1-mdp.learningF) * prev + mdp.learningF * ( reward(s, n) + mdp
                .discountF * valMax(s)) );
    }

    public Action chooseNext(State s) {
        ArrayList<Action> actions = new ArrayList<>();
        try {
            actions = mdp.getActionsList(s);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if( random.nextDouble() <= mdp.observationP ) {
            Action best = mdp.getAction(s, argMax(s));
            if( best!= null )
                return best;
        }
        return actions.get(random.nextInt(actions.size()));
    }

    public abstract Double reward(State s, State n);

    public Double valMax(State s) {
        TreeMap <Long, Double> actions = q.get(s.hash());
        Double maxValue = actions.firstEntry().getValue();
        for(Map.Entry<Long, Double> entry : actions.entrySet()) {
            if( entry.getValue() > maxValue ) {
                maxValue = entry.getValue();
            }
        }
        return maxValue;
    }

    public Long argMax(State s) {
        Long sHash = s.hash();
        if(!q.containsKey(sHash))
            return null;
        TreeMap <Long, Double> actions = q.get(sHash);
        Long bestAction = actions.firstKey();
        Double maxValue = actions.firstEntry().getValue();
        for(Map.Entry<Long, Double> entry : actions.entrySet()) {
            if( entry.getValue() > maxValue ) {
                maxValue = entry.getValue();
                bestAction = entry.getKey();
            }
        }
        return bestAction;
    }
}

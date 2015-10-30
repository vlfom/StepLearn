package com.vlfom.steplearn.core.learning.general;

import com.vlfom.steplearn.core.util.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class QLearning {
    private TreeMap<Long, TreeMap<Long, Double>> q;
    private Random random;
    private MarkovDecisionProcess mdp;

    public QLearning(MarkovDecisionProcess mdp) {
        this.mdp = mdp;
        q = new TreeMap<>();
        random = new Random();
    }

    public MarkovDecisionProcess getMdp() {
        return mdp;
    }

    public State iterateLearning(State state) {
        if (!q.containsKey(state.hash())) {
            populateState(state);
        }

        Action action = chooseNext(state);
        State next = mdp.applyAction(state, action);
        update(state, action, next);
        return next;
    }

    private void populateState(State state) {
        ArrayList<Utils.Pair> actions = mdp.getActionsList(state);
        for (Utils.Pair pair : actions) {
            update(state, (Action) pair.first, (State) pair.second);
        }
    }

    private void update(State s, Action a, State n) {
        long sHash = s.hash();
        if (!q.containsKey(sHash)) {
            q.put(sHash, new TreeMap<>());
        }
        TreeMap<Long, Double> actions = q.get(sHash);
        Long aHash = a.hash();
        if (!actions.containsKey(aHash)) {
            actions.put(aHash, 0.0);
        }
        Double prev = actions.get(aHash);
        actions.put(aHash,
                (1 - mdp.learningF) * prev + mdp.learningF * (mdp.reward(s, a,
                        n) + mdp.discountF * valMax(s)));
    }

    private Action chooseNext(State s) {
        Action argMax = argMax(s);
        if (argMax != null && random.nextDouble() >= mdp.observationP) {
            return argMax;
        }
        ArrayList<Utils.Pair> actions = mdp.getActionsList(s);

        if (actions.size() > 0) {
            return (Action) actions.get(random.nextInt(actions.size())).first;
        }
        return null;
    }

    private Double valMax(State s) {
        TreeMap<Long, Double> actions = q.get(s.hash());
        Double maxValue = actions.firstEntry().getValue();
        for (Map.Entry<Long, Double> entry : actions.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
            }
        }
        return maxValue;
    }

    private Action argMax(State s) {
        Long sHash = s.hash();
        if (!q.containsKey(sHash)) {
            return null;
        }
        TreeMap<Long, Double> actions = q.get(sHash);

        Long bestAction = actions.firstKey();
        Double maxValue = actions.firstEntry().getValue();
        for (Map.Entry<Long, Double> entry : actions.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                bestAction = entry.getKey();
            } else if (entry.getValue()
                    .equals(maxValue) && Math.random() < 0.5) {
                maxValue = entry.getValue();
                bestAction = entry.getKey();
            }
        }
        return mdp.getAction(s, bestAction);
    }

    public Action getArgMax(State s) {
        return argMax(s);
    }
}

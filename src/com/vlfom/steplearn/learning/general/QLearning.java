package com.vlfom.steplearn.learning.general;

import com.vlfom.steplearn.learning.robot.RobotState;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class QLearning {
    TreeMap<Long, TreeMap<Long, Double>> q;
    Random random;
    private MarkovDecisionProcess mdp;

    public QLearning(MarkovDecisionProcess mdp) {
        this.mdp = mdp;
        q = new TreeMap<>();
        random = new Random();
    }

    public State iterate(State state, boolean learning) {
        if (learning && !q.containsKey(state.hash())) {
            populateState(state);
        }

        Action action = chooseNext(state, learning);

        if (action == null) {
            return null;
        }

        State next = action.applyAction(state);

        update(state, action, next, learning);

        return next;
    }

    private void populateState(State state) {
        ArrayList<Action> actions = mdp.getActionsList(state);
        for (Action action : actions) {
            State next = action.applyAction(state);
            update(state, action, next, true);
        }
    }

    private void update(State s, Action a, State n, boolean learning) {
        long sHash = s.hash();
        if (!q.containsKey(sHash)) {
            q.put(sHash, new TreeMap<Long, Double>());
        }
        TreeMap<Long, Double> actions = q.get(sHash);
        Long aHash = a.hash();
        if (!actions.containsKey(aHash)) {
            actions.put(aHash, 0.0);
        }
        Double prev = actions.get(aHash);
        actions.put(aHash, (1 - mdp.learningF) * prev + mdp.learningF * (mdp
                .reward(s, n) + mdp.discountF * valMax(s, learning)));
    }

    private Action chooseNext(State s, boolean learning) {
        ArrayList<Action> actions = mdp.getActionsList(s);
        Long bestHash = argMax(s);
        if (bestHash != null && (random.nextDouble() >= mdp.observationP ||
                !learning)) {
            if (!learning) {
                return mdp.getAction(s, bestHash);
            }
        }
        if (actions.size() > 0) {
            return actions.get(random.nextInt(actions.size()));
        }
        return null;
    }

    private Double valMax(State s, boolean learning) {
        TreeMap<Long, Double> actions = q.get(s.hash());
        Double maxValue = actions.firstEntry().getValue();
        for (Map.Entry<Long, Double> entry : actions.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
            }
        }
        return maxValue;
    }

    private Long argMax(State s) {
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
            }
        }
        return bestAction;
    }
}

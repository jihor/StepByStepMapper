package ru.jihor.mapper.steps;

import lombok.extern.slf4j.Slf4j;
import ru.jihor.mapper.pipelines.Pipeline;
import ru.jihor.mapper.visitors.Visitor;

import java.util.LinkedHashMap;
import java.util.function.Predicate;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
public class SwitchCaseStep<S, T> implements Step {
    private final LinkedHashMap<Predicate<S>, Pipeline> cases = new LinkedHashMap<>();

    public LinkedHashMap<Predicate<S>, Pipeline> getCases() {
        return cases;
    }

    public void addCase(Predicate<S> condition, Pipeline pipeline) {
        log.debug("Registering case # " + (cases.size() + 1));
        if (cases.containsKey(condition)) {
            throw new IllegalArgumentException("Duplicate condition in SwitchCase statement");
        }
        cases.put(condition, pipeline);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}

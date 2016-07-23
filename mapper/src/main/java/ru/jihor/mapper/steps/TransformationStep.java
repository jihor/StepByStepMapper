package ru.jihor.mapper.steps;

import ru.jihor.mapper.base.Step;
import ru.jihor.mapper.base.Visitor;

import java.util.function.BiConsumer;

/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
public class TransformationStep<S, T> extends Step {
    private BiConsumer<S, T> transformation;

    public TransformationStep(BiConsumer<S, T> transformation) {
        this.transformation = transformation;
    }

    public BiConsumer<S, T> getTransformation() {
        return transformation;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
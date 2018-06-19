/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.core.bind;

import io.micronaut.core.bind.exceptions.UnsatisfiedArgumentException;
import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.type.Argument;
import io.micronaut.core.type.Executable;

import java.util.Optional;

/**
 * Default implementation of the {@link ExecutableBinder} interface.
 *
 * @param <S> The source type
 */
public class DefaultExecutableBinder<S> implements ExecutableBinder<S> {

    @Override
    public <T, R> BoundExecutable<T, R> bind(
            Executable<T, R> target,
            ArgumentBinderRegistry<S> registry,
            S source) throws UnsatisfiedArgumentException {

        Argument[] arguments = target.getArguments();
        Object[] boundArguments = new Object[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            Argument<?> argument = arguments[i];
            Optional<? extends ArgumentBinder<?, S>> argumentBinder =
                    registry.findArgumentBinder(argument, source);

            if (argumentBinder.isPresent()) {
                ArgumentBinder<?, S> binder = argumentBinder.get();
                ArgumentConversionContext conversionContext = ConversionContext.of(argument);
                ArgumentBinder.BindingResult<?> bindingResult = binder.bind(
                        conversionContext,
                        source
                );

                if (!bindingResult.isSatisfied()) {
                    throw new UnsatisfiedArgumentException(argument);
                } else {
                    boundArguments[i] = bindingResult.get();
                }

            } else {
                throw new UnsatisfiedArgumentException(argument);
            }
        }

        return new BoundExecutable<T, R>() {
            @Override
            public Executable<T, R> getTarget() {
                return target;
            }

            @Override
            public R invoke(T instance) {
                return target.invoke(instance, boundArguments);
            }
        };
    }
}
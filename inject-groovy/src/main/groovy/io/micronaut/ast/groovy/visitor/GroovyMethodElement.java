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

package io.micronaut.ast.groovy.visitor;

import io.micronaut.ast.groovy.utils.AstAnnotationUtils;
import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.inject.visitor.ClassElement;
import io.micronaut.inject.visitor.MethodElement;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;

/**
 * A method element returning data from a {@link MethodNode}.
 *
 * @author James Kleeh
 * @since 1.0
 */
public class GroovyMethodElement extends AbstractGroovyElement implements MethodElement {

    private final MethodNode methodNode;

    /**
     * @param methodNode         The {@link MethodNode}
     * @param annotationMetadata The annotation metadata
     */
    GroovyMethodElement(MethodNode methodNode, AnnotationMetadata annotationMetadata) {
        super(annotationMetadata);
        this.methodNode = methodNode;
    }

    @Override
    public String getName() {
        return methodNode.getName();
    }

    @Override
    public boolean isAbstract() {
        return methodNode.isAbstract();
    }

    @Override
    public boolean isStatic() {
        return methodNode.isStatic();
    }

    @Override
    public boolean isPublic() {
        return methodNode.isPublic() || methodNode.isSyntheticPublic();
    }

    @Override
    public boolean isPrivate() {
        return methodNode.isPrivate();
    }

    @Override
    public boolean isFinal() {
        return methodNode.isFinal();
    }

    @Override
    public boolean isProtected() {
        return methodNode.isProtected();
    }

    @Override
    public Object getNativeType() {
        return methodNode;
    }

    @Override
    public ClassElement getReturnType() {
        ClassNode returnType = methodNode.getReturnType();
        return new GroovyClassElement(returnType, AstAnnotationUtils.getAnnotationMetadata(returnType));
    }
}

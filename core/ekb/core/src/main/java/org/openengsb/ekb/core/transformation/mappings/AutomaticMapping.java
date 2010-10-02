/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.openengsb.ekb.core.transformation.mappings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openengsb.ekb.api.mapping.TransformationException;
import org.openengsb.ekb.api.mapping.Transformer;

public class AutomaticMapping extends AbstractMapping {

    private List<Class<?>> primitives;

    private Map<Class<?>, Map<Class<?>, Transformer>> primitiveTransformers;

    public AutomaticMapping() {
        primitives = Arrays.asList(new Class<?>[] { Boolean.class, Integer.class, Long.class, Short.class, Byte.class,
                Character.class, Float.class, Double.class });
        primitiveTransformers = new HashMap<Class<?>, Map<Class<?>, Transformer>>();
        primitiveTransformers.put(Boolean.class, createBooleanMap());
        primitiveTransformers.put(Integer.class, createIntegerMap());
        primitiveTransformers.put(Long.class, createLongMap());
        primitiveTransformers.put(Short.class, createShortMap());
        primitiveTransformers.put(Byte.class, createByteMap());
        primitiveTransformers.put(Character.class, createCharacterMap());
        primitiveTransformers.put(Float.class, createFloatMap());
        primitiveTransformers.put(Double.class, createDoubleMap());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T transform(Object source, Class<T> targetType) throws TransformationException {
        if (source == null) {
            return null;
        }
        Class<?> sourceType = source.getClass();

        if (targetType.isAssignableFrom(sourceType)) {
            return targetType.cast(source);
        } else if (targetType.equals(String.class)) {
            return (T) String.valueOf(source);
        } else if (isPrimitiveObjectClass(targetType)) {
            if (sourceType.equals(String.class)) {
                return (T) parseToPrimitive(source.toString(), targetType);
            } else {
                return (T) primitiveToPrimitive(source, targetType);
            }
        }
        throw new TransformationException("Cannot transform automatically from class " + sourceType + " to class "
                + targetType);
    }

    private boolean isPrimitiveObjectClass(Class<?> type) {
        return primitives.contains(type);
    }

    private Object parseToPrimitive(String input, Class<?> targetType) {
        int primitiveIndex = primitives.indexOf(targetType);
        switch (primitiveIndex) {
        case 0:
            return Boolean.valueOf(input);
        case 1:
            return Integer.valueOf(input);
        case 2:
            return Long.valueOf(input);
        case 3:
            return Short.valueOf(input);
        case 4:
            return Byte.valueOf(input);
        case 5:
            return input.charAt(0);
        case 6:
            return Float.valueOf(input);
        case 7:
            return Double.valueOf(input);
        }
        throw new IllegalStateException("Parse to Primitive called with no primitive type.");
    }

    private Object primitiveToPrimitive(Object input, Class<?> targetType) throws TransformationException {
        Map<Class<?>, Transformer> map = primitiveTransformers.get(targetType);
        return map.get(input.getClass()).transform(input, targetType);
    }

    private Map<Class<?>, Transformer> createDoubleMap() {
        Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();

        Transformer numberTransformer = new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Number input = (Number) source;
                return input.doubleValue();
            }
        };

        map.put(Boolean.class, new Transformer() {

            @Override
            public Object transform(Object source, Class<?> targetType) {
                Boolean input = (Boolean) source;
                if (input) {
                    return 1;
                }
                return 0;
            }
        });
        map.put(Integer.class, numberTransformer);
        map.put(Long.class, numberTransformer);
        map.put(Short.class, numberTransformer);
        map.put(Byte.class, numberTransformer);
        map.put(Character.class, new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Character input = (Character) source;
                if (Character.isDigit(input)) {
                    return Double.parseDouble(input.toString());
                }
                return ((Number) Character.getNumericValue(input.charValue())).doubleValue();
            }
        });
        map.put(Float.class, numberTransformer);
        return map;
    }

    private Map<Class<?>, Transformer> createFloatMap() {
        Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();

        Transformer numberTransformer = new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Number input = (Number) source;
                return input.floatValue();
            }
        };

        map.put(Boolean.class, new Transformer() {

            @Override
            public Object transform(Object source, Class<?> targetType) {
                Boolean input = (Boolean) source;
                if (input) {
                    return 1;
                }
                return 0;
            }
        });
        map.put(Integer.class, numberTransformer);
        map.put(Long.class, numberTransformer);
        map.put(Short.class, numberTransformer);
        map.put(Byte.class, numberTransformer);
        map.put(Character.class, new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Character input = (Character) source;
                if (Character.isDigit(input)) {
                    return Float.parseFloat(input.toString());
                }
                return ((Number) Character.getNumericValue(input.charValue())).floatValue();
            }
        });
        map.put(Double.class, numberTransformer);
        return map;
    }

    private Map<Class<?>, Transformer> createCharacterMap() {
        Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();

        Transformer numberTransformer = new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Number input = (Number) source;
                return input.toString().charAt(0);
            }
        };

        map.put(Boolean.class, new Transformer() {

            @Override
            public Object transform(Object source, Class<?> targetType) {
                Boolean input = (Boolean) source;
                if (input) {
                    return 'T';
                }
                return 'F';
            }
        });
        map.put(Integer.class, numberTransformer);
        map.put(Long.class, numberTransformer);
        map.put(Short.class, numberTransformer);
        map.put(Byte.class, numberTransformer);
        map.put(Float.class, numberTransformer);
        map.put(Double.class, numberTransformer);
        return map;
    }

    private Map<Class<?>, Transformer> createByteMap() {
        Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();

        Transformer numberTransformer = new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Number input = (Number) source;
                return input.byteValue();
            }
        };

        map.put(Boolean.class, new Transformer() {

            @Override
            public Object transform(Object source, Class<?> targetType) {
                Boolean input = (Boolean) source;
                if (input) {
                    return 1;
                }
                return 0;
            }
        });
        map.put(Integer.class, numberTransformer);
        map.put(Long.class, numberTransformer);
        map.put(Short.class, numberTransformer);
        map.put(Character.class, new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Character input = (Character) source;
                if (Character.isDigit(input)) {
                    return Byte.parseByte(input.toString());
                }
                return ((Number) Character.getNumericValue(input.charValue())).byteValue();
            }
        });
        map.put(Float.class, numberTransformer);
        map.put(Double.class, numberTransformer);
        return map;
    }

    private Map<Class<?>, Transformer> createShortMap() {
        Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();

        Transformer numberTransformer = new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Number input = (Number) source;
                return input.shortValue();
            }
        };

        map.put(Boolean.class, new Transformer() {

            @Override
            public Object transform(Object source, Class<?> targetType) {
                Boolean input = (Boolean) source;
                if (input) {
                    return 1;
                }
                return 0;
            }
        });
        map.put(Integer.class, numberTransformer);
        map.put(Long.class, numberTransformer);
        map.put(Byte.class, numberTransformer);
        map.put(Character.class, new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Character input = (Character) source;
                if (Character.isDigit(input)) {
                    return Short.parseShort(input.toString());
                }
                return ((Number) Character.getNumericValue(input.charValue())).shortValue();
            }
        });
        map.put(Float.class, numberTransformer);
        map.put(Double.class, numberTransformer);
        return map;
    }

    private Map<Class<?>, Transformer> createLongMap() {
        Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();

        Transformer numberTransformer = new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Number input = (Number) source;
                return input.longValue();
            }
        };

        map.put(Boolean.class, new Transformer() {

            @Override
            public Object transform(Object source, Class<?> targetType) {
                Boolean input = (Boolean) source;
                if (input) {
                    return 1;
                }
                return 0;
            }
        });
        map.put(Integer.class, numberTransformer);
        map.put(Short.class, numberTransformer);
        map.put(Byte.class, numberTransformer);
        map.put(Character.class, new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Character input = (Character) source;
                if (Character.isDigit(input)) {
                    return Long.parseLong(input.toString());
                }
                return ((Number) Character.getNumericValue(input.charValue())).longValue();
            }
        });
        map.put(Float.class, numberTransformer);
        map.put(Double.class, numberTransformer);
        return map;
    }

    private Map<Class<?>, Transformer> createIntegerMap() {
        Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();

        Transformer numberTransformer = new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Number input = (Number) source;
                return input.intValue();
            }
        };

        map.put(Boolean.class, new Transformer() {

            @Override
            public Object transform(Object source, Class<?> targetType) {
                Boolean input = (Boolean) source;
                if (input) {
                    return 1;
                }
                return 0;
            }
        });
        map.put(Long.class, numberTransformer);
        map.put(Short.class, numberTransformer);
        map.put(Byte.class, numberTransformer);
        map.put(Character.class, new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Character input = (Character) source;
                if (Character.isDigit(input)) {
                    return Integer.parseInt(input.toString());
                }
                return Character.getNumericValue(input.charValue());
            }
        });
        map.put(Float.class, numberTransformer);
        map.put(Double.class, numberTransformer);
        return map;
    }

    private Map<Class<?>, Transformer> createBooleanMap() {
        Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();
        Transformer numberTransformer = new Transformer() {
            @Override
            public Object transform(Object source, Class<?> targetType) {
                Number input = (Number) source;
                if (input.doubleValue() > 0) {
                    return true;
                }
                return false;
            }
        };
        map.put(Integer.class, numberTransformer);
        map.put(Long.class, numberTransformer);
        map.put(Short.class, numberTransformer);
        map.put(Byte.class, numberTransformer);
        map.put(Character.class, new Transformer() {

            @Override
            public Object transform(Object source, Class<?> targetType) {
                Character input = (Character) source;
                if (input.charValue() == 't' || input.charValue() == 'T') {
                    return true;
                }
                return false;
            }
        });
        map.put(Float.class, numberTransformer);
        map.put(Double.class, numberTransformer);
        return map;
    }

}

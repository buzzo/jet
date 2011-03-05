package br.unicamp.ic.inject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.unicamp.ic.inject.parameters.metadata.ByteArrayMetadata;
import br.unicamp.ic.inject.parameters.metadata.ByteMetadata;
import br.unicamp.ic.inject.parameters.metadata.DoubleArrayMetadata;
import br.unicamp.ic.inject.parameters.metadata.DoubleMetadata;
import br.unicamp.ic.inject.parameters.metadata.FloatArrayMetadata;
import br.unicamp.ic.inject.parameters.metadata.FloatMetadata;
import br.unicamp.ic.inject.parameters.metadata.IntArrayMetadata;
import br.unicamp.ic.inject.parameters.metadata.IntMetadata;
import br.unicamp.ic.inject.parameters.metadata.MetadataParameter;
import br.unicamp.ic.inject.parameters.metadata.StringArrayMetadata;
import br.unicamp.ic.inject.parameters.metadata.StringMetadata;

public final class ParametersHelper {

    public static final MetadataParameter[] METADATAS = { new IntMetadata(), new IntArrayMetadata(), new ByteArrayMetadata(),
        new ByteMetadata(), new FloatMetadata(), new FloatArrayMetadata(), new DoubleMetadata(), new DoubleArrayMetadata(),
        new StringMetadata(), new StringArrayMetadata() };

    private final Method                    method;

    private ParametersHelper(final Method method) {
        this.method = method;
    }

    /**
     * Indicates if the method contains any annotation for parameter test injection.
     * 
     * @return <code>true</code> there is an annotation for parameter injection.
     */
    public boolean containsInjection() {
        boolean ret = false;
        for (final MetadataParameter metadata : ParametersHelper.METADATAS) {
            ret = ret || metadata.isAnnotationPresent(this.method);
        }
        return ret;
    }

    public int[] getInjectionIndexes() {
        final List<Integer> indexes = new ArrayList<Integer>();
        for (final MetadataParameter metadata : ParametersHelper.METADATAS) {
            if (metadata.isAnnotationPresent(this.method)) {
                indexes.add(metadata.getPosition(this.method));
            }
        }
        final int[] ret = new int[indexes.size()];
        for (int i = 0; i < indexes.size(); i++) {
            ret[i] = indexes.get(i);
        }
        return ret;
    }

    public Object getInjectionValue(final int index) {
        for (final MetadataParameter metadata : ParametersHelper.METADATAS) {
            if (metadata.isAnnotationPresent(this.method) && metadata.getPosition(this.method) == index) {
                return metadata.getValue(this.method);
            }
        }
        return null;
    }

    public static ParametersHelper newInstance(final Method method) {
        return new ParametersHelper(method);
    }
}

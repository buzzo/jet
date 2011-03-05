package br.unicamp.ic.inject;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;

import br.unicamp.ic.inject.parameters.IntArrayParameter;
import br.unicamp.ic.inject.parameters.IntParameter;

public class ParametersHelperTest {

    @Test
    public void testHelper() throws SecurityException, NoSuchMethodException {
        final Method m = Bla.class.getMethod("blu", new Class<?>[] {});
        Assert.assertTrue("Method contains an injection.", ParametersHelper.newInstance(m).containsInjection());
        final int[] indexes = ParametersHelper.newInstance(m).getInjectionIndexes();
        Assert.assertEquals("Wrong array size.", 2, indexes.length);
        Assert.assertEquals("Unexpected index.", 1, indexes[0]);
        Assert.assertEquals("Unexpected index.", 2, indexes[1]);
        Assert.assertEquals("Unexpected value for index.", 1, ParametersHelper.newInstance(m).getInjectionValue(1));
    }

    public static class Bla {
        @IntParameter(index = 1, value = 1)
        @IntArrayParameter(index = 2, value = { 1, 2 })
        public void blu() {

        }
    }

}

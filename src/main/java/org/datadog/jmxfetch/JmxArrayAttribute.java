package org.datadog.jmxfetch;

import javax.management.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class JmxArrayAttribute extends JmxSimpleAttribute {
    private Metric cachedMetric;

    /** JmxArrayAttribute constructor. */
    public JmxArrayAttribute(
            MBeanAttributeInfo attribute,
            ObjectName beanName,
            String className,
            String instanceName,
            String checkName,
            Connection connection,
            Map<String, String> instanceTags,
            boolean cassandraAliasing,
            Boolean emptyDefaultHostname) {
        super(
                attribute,
                beanName,
                className,
                instanceName,
                checkName,
                connection,
                instanceTags,
                cassandraAliasing,
                emptyDefaultHostname);
    }

    @Override
    public List<Metric> getMetrics()
            throws AttributeNotFoundException, InstanceNotFoundException, MBeanException,
            ReflectionException, IOException {
        if (cachedMetric == null) {
            String alias = getAlias(null);
            String metricType = getMetricType(null);
            String[] tags = getTags();
            cachedMetric = new Metric(alias, metricType, tags, checkName);
        }
        double value = castToDouble(getValue(), null);
        cachedMetric.setValue(value);
        return Collections.singletonList(cachedMetric);
    }

    private Object getValue()
            throws AttributeNotFoundException, InstanceNotFoundException, MBeanException,
            ReflectionException, IOException, NumberFormatException {
        return ((ObjectName[]) this.getJmxValue()).length;
    }
}

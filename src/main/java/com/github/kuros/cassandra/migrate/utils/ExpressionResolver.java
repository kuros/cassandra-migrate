package com.github.kuros.cassandra.migrate.utils;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionResolver {

    static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{([^}]*)\\}");

    /**
     * Replace ${properties} in an expression
     * @param expression expression string
     * @param properties property map
     * @return resolved expression string
     */
    public static String resolveExpression(String expression, Properties properties) {
        if (properties == null) {
            return expression;
        }

        StringBuilder result = new StringBuilder(expression.length());
        int i = 0;
        Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
        while(matcher.find()) {
            // Strip leading "${" and trailing "}" off.
            result.append(expression.substring(i, matcher.start()));
            String property = matcher.group();
            property = property.substring(2, property.length() - 1);
            if(properties.containsKey(property)) {
                //look up property and replace
                property = properties.getProperty(property);
            } else {
                //property not found, don't replace
                property = matcher.group();
            }
            result.append(property);
            i = matcher.end();
        }
        result.append(expression.substring(i));
        return result.toString();
    }
}

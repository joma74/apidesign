package at.joma.apidesign.component.l2.provider.impl;

import java.util.Arrays;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import at.joma.apidesign.component.l2.client.api.IL2Component;
import at.joma.apidesign.component.l2.client.api.f.Omitting;
import at.joma.apidesign.component.l2.client.api.f.Sorting;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingDirection;
import at.joma.apidesign.component.l2.client.api.f.sorting.SortingOrder;
import at.joma.apidesign.component.l2.provider.api.AsXML;

@Sorting
@Omitting
public class ComponentProducer {

    @Produces
    @AsXML(ommiting = @Omitting, sorting = @Sorting)
    public IL2Component doProduce(InjectionPoint ip) throws NoSuchMethodException {
        
        this.getClass().getAnnotation(Sorting.class).direction();

        SortingOrder orderOption = this.getClass().getAnnotation(Sorting.class).order();
        SortingDirection directionOption = this.getClass().getAnnotation(Sorting.class).direction();
        String[] globalFieldsOption = this.getClass().getAnnotation(Omitting.class).globalFields();
        
        Annotated annotated = ip.getAnnotated();
        
        if(annotated != null){

            Sorting sortingAnnotation = annotated.getAnnotation(Sorting.class);

            Omitting omittingAnnotation = annotated.getAnnotation(Omitting.class);
            
            if (sortingAnnotation != null) {
                orderOption = sortingAnnotation.order();
                directionOption = sortingAnnotation.direction();
            }

            if (omittingAnnotation != null) {
                globalFieldsOption = omittingAnnotation.globalFields();
            }
        }

        Configured iL2Component = new Configured();
        iL2Component.orderOption = orderOption;
        iL2Component.directionOption = directionOption;
        iL2Component.globalFieldsOption = globalFieldsOption;

        return iL2Component;
    }

    public static class Configured implements IL2Component {

        private static final String TAB = "\t";

        public SortingOrder orderOption;

        public SortingDirection directionOption;

        public String[] globalFieldsOption;

        @Override
        public String serialize(Object serializable) {
            return null;
        }

        public String printConfiguration() {
            StringBuilder configuration = new StringBuilder(System.lineSeparator() + "Configuration" + System.lineSeparator());
            configuration.append(TAB + orderOption.getClass().getSimpleName() + ":" + orderOption.toString() + System.lineSeparator());
            configuration.append(TAB + directionOption.getClass().getSimpleName() + ":" + directionOption.toString()  + System.lineSeparator());
            configuration.append(TAB + Omitting.class.getSimpleName() + ":" + Arrays.toString(globalFieldsOption)  + System.lineSeparator());
            return configuration.toString();
        }
        
        @Override
        public String toString() {
            return printConfiguration();
        }
    }

}

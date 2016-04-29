package at.joma.apidesign.component.l2.client.api;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;

import at.joma.apidesign.component.l1.client.api.IL1Component;
import at.joma.apidesign.component.l2.client.api.builder.IRequiredProviderOptions;
import at.joma.apidesign.component.l2.client.api.builder.IWithConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.builder.IWithOmitting;
import at.joma.apidesign.component.l2.client.api.builder.IWithSorting;
import at.joma.apidesign.component.l2.client.api.builder.options.OmittingOptions;
import at.joma.apidesign.component.l2.client.api.builder.options.SortingOptions;
import at.joma.apidesign.component.l2.client.api.types.SortingDirection;
import at.joma.apidesign.component.l2.client.api.types.SortingOrder;
import at.joma.apidesign.component.l2.client.api.types.config.ConfiguredOptionsHolder;
import at.joma.apidesign.component.l2.client.api.types.config.Option;

public interface IL2Component extends IL1Component {

    @Sorting
    @Omitting
    public class Builder<T extends IRequiredProviderOptions & Annotation> implements IWithSorting<T>, IWithOmitting<T>, IWithConfiguredOptionsHolder<T> {

        private ConfiguredOptionsHolder configuredOptionsHolder = new ConfiguredOptionsHolder();

        private final Class<T> providerType;

        public Builder(Class<T> providerType) {

            this.providerType = providerType;

            this.configuredOptionsHolder.with(this.getClass().getAnnotation(Sorting.class).order());
            this.configuredOptionsHolder.with(this.getClass().getAnnotation(Sorting.class).direction());
            this.configuredOptionsHolder.with(Omitting.BYFIELDNAMES_OPTIONNAME, this.getClass().getAnnotation(Omitting.class).byFieldNames());
            this.configuredOptionsHolder.with(Omitting.BYFIELDANNOTATIONS_OPTIONNAME, this.getClass().getAnnotation(Omitting.class).byFieldAnnotations());
            this.configuredOptionsHolder.with(Omitting.BYFIELDCLASSES_OPTIONNAME, this.getClass().getAnnotation(Omitting.class).byFieldClasses());
        }

		@SuppressWarnings("unchecked")
		public IL2Component build() throws ReflectiveOperationException {

            SortingOptions sortingAnnotation = SortingOptions.class.newInstance();
            sortingAnnotation.setSortingDirection(this.configuredOptionsHolder.getValueFor(SortingDirection.class));
            sortingAnnotation.setSortingOrder(this.configuredOptionsHolder.getValueFor(SortingOrder.class));

            OmittingOptions omittingAnnotation = OmittingOptions.class.newInstance();
            omittingAnnotation.setByFieldNames((String[]) this.configuredOptionsHolder.getValueFor(Omitting.BYFIELDNAMES_OPTIONNAME));
            omittingAnnotation.setByFieldAnnotations((Class<? extends Annotation>[])this.configuredOptionsHolder.getValueFor(Omitting.BYFIELDANNOTATIONS_OPTIONNAME));
            omittingAnnotation.setByFieldClasses((Class<?>[]) this.configuredOptionsHolder.getValueFor(Omitting.BYFIELDCLASSES_OPTIONNAME));

            T providerTypeInstance = providerType.newInstance();
            providerTypeInstance.setSortingOption(sortingAnnotation);
            providerTypeInstance.setOmittingOption(omittingAnnotation);

            Instance<IL2Component> inst = CDI.current().select(IL2Component.class, providerTypeInstance);

            IL2Component beanInstance = inst.get(); // NOSONAR

            return beanInstance;
        }

        @Override
        public Builder<T> with(ConfiguredOptionsHolder configuredOptionsHolder) {
            for(Option option : configuredOptionsHolder.getOptions()){
                this.configuredOptionsHolder.with(option);
                this.configuredOptionsHolder.encloseFormatInfos(configuredOptionsHolder.getFormatInfo());
            }
            return this;
        }

        @Override
        public Builder<T> with(SortingDirection sortingDirection) {
            this.configuredOptionsHolder.with(sortingDirection);
            return this;
        }

        @Override
        public Builder<T> with(SortingOrder sortingOrder) {
            this.configuredOptionsHolder.with(sortingOrder);
            return this;
        }

        @Override
        public Builder<T> with(String[] globalFields) {
            this.configuredOptionsHolder.with(Omitting.BYFIELDNAMES_OPTIONNAME, globalFields);
            return this;
        }
    }

}

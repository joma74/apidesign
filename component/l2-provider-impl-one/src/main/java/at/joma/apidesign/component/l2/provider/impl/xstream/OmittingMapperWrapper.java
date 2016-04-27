package at.joma.apidesign.component.l2.provider.impl.xstream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class OmittingMapperWrapper extends MapperWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(OmittingMapperWrapper.class);

	private final Set<String> omittingByFieldNames;

	private final Set<Annotation> omitByFieldAnnotations;

	private final Set<Class<?>> omitByFieldClasses;

	public OmittingMapperWrapper(final Mapper wrapped, final String[] omittingByFieldNames, final Set<Annotation> omittingByFieldAnnotations,
			final Set<Class<?>> omittingByFieldClasses) {
		super(wrapped);
		Preconditions.checkNotNull(omittingByFieldNames);
		this.omittingByFieldNames = new HashSet<>(Arrays.asList(omittingByFieldNames));
		Preconditions.checkNotNull(omittingByFieldAnnotations);
		this.omitByFieldAnnotations = omittingByFieldAnnotations;
		Preconditions.checkNotNull(omittingByFieldClasses);
		this.omitByFieldClasses = omittingByFieldClasses;
	}

	@Override
	public boolean shouldSerializeMember(@SuppressWarnings("rawtypes") final Class definedIn, final String fieldName) {
		final Field theField = getFieldFrom(definedIn, fieldName);
		if (theField != null && (isFieldWithOmitName(theField) || isFieldWithOmitAnnotation(theField) || isFieldWithOmitClass(theField))) {
			return false;
		}
		return super.shouldSerializeMember(definedIn, fieldName);
	}

	protected boolean isFieldWithOmitName(Field theField) {
		return omittingByFieldNames.contains(theField.getName());
	}

	protected boolean isFieldWithOmitAnnotation(final Field field) {
		Annotation[] annotations = field.getAnnotations();
		if (annotations.length == 0) {
			return false;
		} else {
			return !Collections.disjoint(Arrays.asList(annotations), omitByFieldAnnotations);
		}

	}

	protected boolean isFieldWithOmitClass(Field theField) {
		for (Class<?> omittingByFieldClass : omitByFieldClasses) {
			if (omittingByFieldClass.isAssignableFrom(theField.getClass())) {
				return true;
			}
		}
		return false;
	}

	protected Field getFieldFrom(final Class<?> definedIn, final String fieldName) {
		try {
			return definedIn.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e) {
			LOG.trace("Ignored exception", e);
		}
		return null;
	}
}

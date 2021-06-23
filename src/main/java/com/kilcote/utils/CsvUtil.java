package com.kilcote.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kilcote.common.csv.CustomBeanToCSVMappingStrategy;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CsvUtil {

	@SuppressWarnings("unchecked")
	public static <T> void writeFromDatas(List<T> list, Writer writer, @SuppressWarnings("rawtypes") Class clazz) throws Exception {
		CustomBeanToCSVMappingStrategy<T> mappingStrategy = new CustomBeanToCSVMappingStrategy<T>();
        mappingStrategy.setType(clazz);
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(mappingStrategy)
                .build();

        beanToCsv.write(list);
	}
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertFromCsv(MultipartFile file, @SuppressWarnings("rawtypes") Class clazz) throws IOException {
		@SuppressWarnings("rawtypes")
		ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<T>();
	    strategy.setType(clazz);

		try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
					.withType(clazz)
					.withSkipLines(1)
					.withMappingStrategy(strategy)
					.build();
			return csvToBean.parse();
		}
	}

	public static <T> String convertToCsv(List<T> entitiesList, MappingStrategy<T> mappingStrategy) throws Exception {
		try (Writer writer = new StringWriter()) {
			StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
					.withMappingStrategy(mappingStrategy)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
					.build();
			beanToCsv.write(entitiesList);
			return writer.toString();
		}
	}

}

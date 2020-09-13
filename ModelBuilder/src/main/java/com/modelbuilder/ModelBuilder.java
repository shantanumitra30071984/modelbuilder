package com.modelbuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

/**
 * ModelBuilder
 *
 */
public class ModelBuilder {
	private static String MODEL_PATH = "src//main//java//";

	public static void main(String[] args) {
		StringBuilder content = null;
		String classBuilder = null;
		try {
			FileInputStream inputStream = new FileInputStream("src/main/resources/model.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			String packageName = properties.getProperty("model-package");
			String replacedPackage = packageName.replace(".", "//");
			Integer index = Integer.valueOf(properties.getProperty("last_index"));
			for (int i = 0; i < index; i++) {
				MODEL_PATH = MODEL_PATH + replacedPackage + "//";
				String property = properties.getProperty("model_" + i);
				System.out.println("property::" + property);
				String[] fileAndFields = property.split("\\.");
				String fileName = fileAndFields[0] + ".java";
				File file = new File(MODEL_PATH + fileName);
				System.out.println(file.exists());
				if (!file.exists()) {
					file.createNewFile();
				}
				content = new StringBuilder();
				content.append("package " + packageName + ";" + "\n");
				content.append("import lombok.Data;\n");
				content.append("@Data\n");

				classBuilder = "public class " + fileAndFields[0] + "{\n";
				for (int j = 1; j < fileAndFields.length; j++) {
					if (fileAndFields[j].contains("_")) {
						String type = fileAndFields[j].split("_")[1];
						String objectProperty = fileAndFields[j].split("_")[0];
						classBuilder = classBuilder + "private " + type + " " + objectProperty + ";\n";
					} else {
						classBuilder = classBuilder + "private String " + fileAndFields[j] + ";\n";
					}
				}
				classBuilder = classBuilder + "}";
				classBuilder = content.toString() + classBuilder;
				BufferedWriter buff = new BufferedWriter(new FileWriter(file));
				buff.write(classBuilder.toString());
				buff.close();
				content = null;

				classBuilder = null;
				MODEL_PATH = "src//main//java//";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

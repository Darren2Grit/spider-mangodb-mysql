package cn.xueyuetang.questionspider.generator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * <p>
 * 测试生成代码
 * </p>
 *
 * @author K神
 * @date 2017/12/18
 */
public class GeneratorServiceEntity {

	@Test
	public void generateCode() {
		String packageName = "cn.xueyuetang.questionspider";
		boolean serviceNameStartWithI = true;// user -> UserService, 设置成true:
												// user -> IUserService
		generateByTables(serviceNameStartWithI, packageName, "tm_question","tm_course_res","tm_knowledge");
	}

	private void generateByTables(boolean serviceNameStartWithI, String packageName, String... tableNames) {
		final String projectPath = System.getProperty("user.dir");
		GlobalConfig config = new GlobalConfig();
		AutoGenerator  mpg=new AutoGenerator();
		String dbUrl = "jdbc:mysql://localhost:3306/xyt";
		DataSourceConfig dataSourceConfig = new DataSourceConfig();
		dataSourceConfig.setDbType(DbType.MYSQL).setUrl(dbUrl).setUsername("root").setPassword("root")
				.setDriverName("com.mysql.jdbc.Driver");
		StrategyConfig strategyConfig = new StrategyConfig();
		strategyConfig.setCapitalMode(true).setEntityLombokModel(false).setNaming(NamingStrategy.underline_to_camel)
				.setInclude(tableNames);// 修改替换成你需要的表名，多个表名传数组
		config.setActiveRecord(false).setAuthor("darren").setOutputDir(projectPath+"/src/main/java").setFileOverride(true);
		config.setMapperName("%sDao");
		if (!serviceNameStartWithI) {
			config.setServiceName("%sService");
		}
		InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig() {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/src/main/resources/mybatis/mapper/" 
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));
		
        mpg.setGlobalConfig(config).setDataSource(dataSourceConfig).setStrategy(strategyConfig)
				.setPackageInfo(
						new PackageConfig().setParent(packageName).setEntity("entity").setMapper("dao").setXml("resources.mybatis.mapper"))
				.execute();
	}

	private void generateByTables(String packageName, String... tableNames) {
		generateByTables(true, packageName, tableNames);
	}
}

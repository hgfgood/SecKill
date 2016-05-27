package testmysql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库测试大约插入数据库1400条数据/s
 *
 * Created by hgf on 16-5-27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
@ImportResource(locations = "jdbc.properties")
public class UpdateQpsTest {

    @Value("${driver}")
    private String driver;

    @Value("${url}")
    private String url;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Test
    public void MysqlUpdateTest(){

        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url,username,password);
            Statement statement = connection.createStatement();
            int i = 0;
            while(i < 10000){
                String sql = "insert into test(b,c,d) values('" +
                        ((int)(Math.random()*100))+"',NOW(),"+"'111111111'"+
                        ")";
                System.out.println(sql);
                statement.execute(sql);

                i++;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

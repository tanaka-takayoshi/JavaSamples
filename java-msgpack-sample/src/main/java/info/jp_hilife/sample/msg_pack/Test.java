package info.jp_hilife.sample.msg_pack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;

public class Test {

	public static class JavaBeansEntity implements Serializable {
		private static final long serialVersionUID = -4102186949619878656L;
		private String str;
		private double num;
		private transient DataSource dataSource;
	
		public String getStr() {
			return str;
		}
	
		public void setStr(String str) {
			this.str = str;
		}
	
		public double getNum() {
			return num;
		}
	
		public void setNum(double num) {
			this.num = num;
		}
	
		public DataSource getDataSource() {
			return dataSource;
		}
	
		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}
	
		private void writeObject(ObjectOutputStream oos) throws IOException {
			oos.defaultWriteObject();
			oos.writeObject(str);
			oos.writeDouble(num);
		}
	
		private void readObject(ObjectInputStream ois) throws IOException,
				ClassNotFoundException {
			ois.defaultReadObject();
			str = (String) ois.readObject();
			num = (double) ois.readDouble();
			str = "Copied Instance";
		}
	
		@Override
		public String toString() {
			return "JavaBeansEntity [str=" + str + ", num=" + num
					+ ", dataSource=" + dataSource + "]";
		}
	}

	@Message
	public static class MessageEntity {
		public String str;
		public double num;

		@Override
		public String toString() {
			return "MessageEntity [str=" + str + ", num=" + num + "]";
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		MessageEntity src0 = new MessageEntity();
		src0.str = "msgpack";
		src0.num = 0.6;

		JavaBeansEntity src1 = new JavaBeansEntity();
		src1.setStr("msgpack");
		src1.setNum(0.6);
		src1.setDataSource(new MyDataSource());

		MessagePack msgpack = new MessagePack();
		msgpack.register(JavaBeansEntity.class);

		System.out.println("MessagePack for Message Annotation");
		test(msgpack, src0);
		System.out.println("MessagePack for JavaBeans");
		test(msgpack, src1);
		System.out.println("ObjectStream");
		testObjectStream(src1);
	}

	private static void test(MessagePack msgpack, Object src)
			throws IOException {
		// Serialize
		byte[] raw = msgpack.write(src);

		System.out.println(Arrays.toString(raw));
		// Deserialize
		Object dst = msgpack.read(raw, src.getClass());

		System.out.println(dst);
	}

	private static void testObjectStream(JavaBeansEntity entity) throws IOException {
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(baos);
			out.writeObject(entity);
			out.close();
			out = null;

			in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
			JavaBeansEntity dst = (JavaBeansEntity)in.readObject();
			System.out.println(dst);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static class MyDataSource implements DataSource {

		@Override
		public PrintWriter getLogWriter() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLogWriter(PrintWriter out) throws SQLException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setLoginTimeout(int seconds) throws SQLException {
			// TODO Auto-generated method stub

		}

		@Override
		public int getLoginTimeout() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Connection getConnection() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Connection getConnection(String username, String password)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

	}
}

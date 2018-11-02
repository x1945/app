package app.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class libSVMdemo {

	svm_parameter _param;
	svm_problem _prob;
	String _model_file = "svm_model.txt";

	protected void loadData(boolean is_training) {
		String limit;
		if (is_training) { // training
			System.out.print("Loading training data...");
			limit = " WHERE id <= 4700";
		} else {
			System.out.print("Loading testing data...");
			limit = " WHERE id > 4700";
			// limit = " WHERE id <= 4700";
		}

		int max_index = 0;
		_prob = new svm_problem();
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:sparseData.s3db");
			// conn = DriverManager.getConnection("jdbc:sqlite:#classpath:sparseData.s3db");
			stat = conn.createStatement();
			rs = stat.executeQuery("SELECT * FROM data" + limit);

			while (rs.next()) {
				vy.addElement(rs.getDouble("label"));

				int rdk1 = rs.getInt("rdk1"), rdk2 = rs.getInt("rdk2");
				if (rdk1 == rdk2) { // 兩個index相等只放一個
					svm_node[] x = new svm_node[1];
					x[0] = new svm_node();
					x[0].index = rdk1;
					x[0].value = 1;
					max_index = Math.max(max_index, rdk1);
					vx.addElement(x);
				} else {
					if (rdk2 < rdk1) { // 如果第二個index比第一個小，交換
						rdk1 = rdk2;
						rdk2 = rs.getInt("rdk1");
					}

					svm_node[] x = new svm_node[2];
					x[0] = new svm_node();
					x[0].index = rdk1;
					x[0].value = 1;
					x[1] = new svm_node();
					x[1].index = rdk2;
					x[1].value = 1;
					max_index = Math.max(max_index, rdk2);
					vx.addElement(x);
				}
			}
			rs.close();
			conn.close();
		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					rs = null;
				}
			}
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					stat = null;
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					conn = null;
				}
			}
		}

		if (max_index > 0)
			_param.gamma = 1.0 / max_index; // 1/num_features

		_prob.l = vy.size();
		System.out.println(_prob.l);
		_prob.x = new svm_node[_prob.l][];
		for (int i = 0; i < _prob.l; i++)
			_prob.x[i] = vx.elementAt(i);
		_prob.y = new double[_prob.l];
		for (int i = 0; i < _prob.l; i++)
			_prob.y[i] = vy.elementAt(i);

		System.out.println("Done!!");
	}

	protected void training() {
		loadData(true);

		System.out.print("Training...");
		try {
			svm_model model = svm.svm_train(_prob, _param);
			System.out.println("Done!!");
			svm.svm_save_model(_model_file, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void testing() {
		loadData(false);

		System.out.print("Training...");
		svm_model model;
		int correct = 0, total = 0;
		try {
			model = svm.svm_load_model(_model_file);

			for (int i = 0; i < _prob.l; i++) {
				double v;
				svm_node[] x = _prob.x[i];
				v = svm.svm_predict(model, x);
				total++;
				// System.out.println(i + ". [" + _prob.y[i] + "], v[" + v + "]");
				if (v == _prob.y[i])
					correct++;
			}

			double accuracy = (double) correct / total * 100;
			System.out.println("Accuracy = " + accuracy + "% (" + correct + "/" + total + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	libSVMdemo() {
		// default values
		_param = new svm_parameter();

		_param.svm_type = svm_parameter.C_SVC;
		_param.kernel_type = svm_parameter.LINEAR;
		_param.degree = 3;
		_param.gamma = 0; // 1/num_features
		_param.coef0 = 0;
		_param.nu = 0.5;
		_param.cache_size = 100;
		_param.C = 1;
		_param.eps = 1e-3;
		_param.p = 0.1;
		_param.shrinking = 1;
		_param.probability = 0;
		_param.nr_weight = 0;
		_param.weight_label = new int[0];
		_param.weight = new double[0];
	}

	public static void main(String[] args) {
		libSVMdemo ld = new libSVMdemo();
		ld.training();
		ld.testing();
		// ld.parseVector();
	}

	public void parseVector() {
		// 關鍵字
		StringBuilder key = new StringBuilder();
		key.append("任職期間、三級管制、施政計畫、管考業務、評核成績、優等、獎勵、考績委員會、獎勵額度、嘉奬");
		String[] keys = key.toString().split("、");
		// 主旨
		StringBuilder str = new StringBuilder();
		str.append("貴會、科長、資訊中心、任職期間、辦理、三級管制、施政計畫、管考業務、評核成績、優等、惠予獎勵");
		// 說明
		str.append("、");
		str.append("考績委員會、會議決議、獎勵額度、嘉奬");

		String[] strs = str.toString().split("、");
		Set<String> contents = new HashSet<String>();
		for (String s : strs) {
			contents.add(s);
		}

		parseVector(keys, contents);
	}

	public void parseVector(String[] keys, Set<String> contents) {
		Vector<Integer> result = new Vector<Integer>();
		for (String key : keys)
			result.addElement(contents.contains(key) ? 1 : 0);
		System.out.println(result);
	}
}

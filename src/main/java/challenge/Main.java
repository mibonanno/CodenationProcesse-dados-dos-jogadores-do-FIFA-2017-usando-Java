package challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

		private String filePath = System.getProperty("user.dir") + "/src/main/resources/data.csv";
		private Map<String, Integer> columns = new HashMap<>();
		private List<String[]> players = new ArrayList<>();

	public Main() {
		setData();
	}

		private void setData() {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

			int index = 0;

			// guarda os jogadores
			String hardData = "";
			while((hardData = br.readLine()) != null) {
				String[] line = hardData.split(",");

				if (index == 0) {
					// mapeia as colunas
					for (int count = 0; count < line.length; count++) {
						this.columns.put(line[count], count);
					}
					index = 1;
				}
				else players.add(line);
			}

			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

		// Quantas nacionalidades (coluna `nationality`) diferentes existem no arquivo?
		public int q1() {
		ArrayList<String> nationalitys = new ArrayList<>();

		for (String[] player : players) {
			String nationality = player[columns.get("nationality")];

			if (! nationalitys.contains(nationality)) nationalitys.add(nationality);
		}

		return nationalitys.size();
	}

		// Quantos clubes (coluna `club`) diferentes existem no arquivo?
		// Obs: Existem jogadores sem clube.
		public int q2() {
		ArrayList<String> clubs = new ArrayList<>();

		for (String[] player : players) {
			String club = player[columns.get("club")];

			if ((! club.isEmpty() || club != null) && (! clubs.contains(club))) clubs.add(club);
		}

		return clubs.size() - 1;
	}

		// Liste o nome completo (coluna `full_name`) dos 20 primeiros jogadores.
		public List<String> q3() {
		return players.stream()
				.map(x -> x[columns.get("full_name")])
				.limit(20)
				.collect(Collectors.toList());
	}

		// Quem são os top 10 jogadores que possuem as maiores cláusulas de rescisão?
		// (utilize as colunas `full_name` e `eur_release_clause`)
		public List<String> q4() {
		List<String> names = players.stream()
				.sorted(Comparator.comparing(x -> {
					if (x[columns.get("eur_release_clause")].isEmpty() || x[columns.get("eur_release_clause")] == null) return 0D;
					return Double.parseDouble(x[columns.get("eur_release_clause")]);
				}))
				.map(x -> x[columns.get("full_name")])
				.collect(Collectors.toList());

		Collections.reverse(names);

		return names.stream().limit(10).collect(Collectors.toList());
	}

		// Quem são os 10 jogadores mais velhos (use como critério de desempate o campo `eur_wage`)?
		// (utilize as colunas `full_name` e `birth_date`)
		public List<String> q5() {
		List<String> names = players.stream()
				.sorted(Comparator.comparing(x -> {
					if (x[columns.get("birth_date")].isEmpty()) return LocalDate.now();
					return LocalDate.parse(x[columns.get("birth_date")]);
				}))
				.map(x -> x[columns.get("full_name")])
				.collect(Collectors.toList());

		return names.stream().limit(10).collect(Collectors.toList());
	}

		// Conte quantos jogadores existem por idade. Para isso, construa um mapa onde as chaves são as idades e os valores a contagem.
		// (utilize a coluna `age`)
		public Map<Integer, Integer> q6() {
		Map<Integer, Integer> ages = new HashMap<>();

		for (String[] player : players) {
			String age  = player[columns.get("age")];

			if (! ages.containsKey(age))
				ages.put(Integer.parseInt(age), Integer.parseInt(ageFrequency(age).toString()));
		}
		return ages;
	}

		private Long ageFrequency(String age) {
		return players.stream()
				.filter(x -> x[columns.get("age")].equals(age))
				.count();
	}

		public static void main(String[] args) {
		Main main = new Main();

		System.out.println("Nacionalidades = " + main.q1());
		System.out.println("Clubes = " + main.q2());
		System.out.println("20 Primeiros nomes = " + main.q3());
		System.out.println("10 Jogadores mais caros = " + main.q4());
		System.out.println("10 Jogadores mais velhos = " + main.q5());
		System.out.println("Jogadores por idade = " + main.q6());
	}

	}

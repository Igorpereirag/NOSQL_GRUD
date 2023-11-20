import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Repository {

    private JSONObject bancoDeDados;
    private JSONTokener tokener;

    public Repository() throws FileNotFoundException {
        this.tokener = new JSONTokener(new FileReader("banco_de_dados.json"));
        this.bancoDeDados = new JSONObject(this.tokener);
    }

    public void GetUserbyId(int id){
        try {
            JSONArray usuarios = bancoDeDados.getJSONArray("pessoas");
            System.out.println("usuario encontrado:");
            // Itere sobre os registros e exiba os dados
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject pessoa = usuarios.getJSONObject(i);
                if(pessoa.getInt("id") == id){
                String nome = pessoa.getString("nome");
                int idade = pessoa.getInt("idade");
                System.out.println("ID: " + id);
                System.out.println("Nome: " + nome);
                System.out.println("Idade: " + idade);
                System.out.println("*---------*");
                }
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetUsuarios() {
        try {
            JSONArray usuarios = bancoDeDados.getJSONArray("pessoas");
            System.out.println("Registros no banco de dados:");
            // Itere sobre os registros e exiba os dados
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject pessoa = usuarios.getJSONObject(i);
                int id = pessoa.getInt("id");
                String nome = pessoa.getString("nome");
                int idade = pessoa.getInt("idade");

                System.out.println("ID: " + id);
                System.out.println("Nome: " + nome);
                System.out.println("Idade: " + idade);
                System.out.println("*---------*");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(int id, String nome, int idade) {
        try {
            JSONArray usuarios = bancoDeDados.getJSONArray("pessoas");

            // Verificar se o ID já existe e remover duplicatas
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject pessoa = usuarios.getJSONObject(i);
                if (pessoa.getInt("id") == id) {
                    System.out.println("ID " + id + " já existe. Removendo a duplicata.");
                    usuarios.remove(i);
                    break; // Saia do loop após remover a duplicata
                }
            }

            // Adicione o novo usuário
            JSONObject newUser = new JSONObject();
            newUser.put("id", id);
            newUser.put("nome", nome);
            newUser.put("idade", idade);
            usuarios.put(newUser);

            // Salvar as alterações de volta no arquivo JSON
            savedata();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        try {
            JSONArray usuarios = bancoDeDados.getJSONArray("pessoas");
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject pessoa = usuarios.getJSONObject(i);
                if (pessoa.getInt("id") == id) {
                    usuarios.remove(i);
                    // Salvar as alterações de volta no arquivo JSON
                    savedata();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savedata() {
        try (FileWriter fileWriter = new FileWriter("banco_de_dados.json")) {
            fileWriter.write(bancoDeDados.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeusersduplications() {

        try {
            JSONArray usuarios = bancoDeDados.getJSONArray("pessoas");
            Set<Integer> ids = new HashSet<>(); // Para rastrear IDs duplicados

            for (int i = usuarios.length() - 1; i >= 0; i--) {
                JSONObject pessoa = usuarios.getJSONObject(i);
                int id = pessoa.getInt("id");

                if (ids.contains(id)) {
                    // ID duplicado encontrado, remova o usuário
                    System.out.println("Removendo usuário com ID duplicado: " + id);
                    usuarios.remove(i);
                } else {
                    ids.add(id); // Adicione o ID à lista de IDs vistos
                }
            }

            // Salvar as alterações de volta no arquivo JSON
            savedata();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
   
    public void updateUser(int id, String nome, int idade) {
        try {
            JSONArray usuarios = bancoDeDados.getJSONArray("pessoas");
            for (int i = 0; i < bancoDeDados.length(); i++) {
                JSONObject pessoa = usuarios.getJSONObject(i);
                if (pessoa.getInt("id") == id) {
                    System.out.println("ID " + id + " já existe. Atualizando informações.");
                    pessoa.put("nome", nome);
                    pessoa.put("idade", idade);
                    
                    break;
                }
            }
            
            savedata();
        } catch (Exception e) {
            throw new RuntimeException("Usuário não existe no banco de dados, tente adicionar um existente");
        }
    }


}

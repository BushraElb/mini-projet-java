package metier;

import modele.Client;
import modele.CompteBancaire;

import java.util.List;

public interface ITraitement {

    // client
    void ajouterClient(Client client);
    void modifierClient(Client client);
    void supprimerClient(int id);
    Client getClient(int id);
    List<Client> getClients();

    // compte bancaire
    void ajouterCompte(CompteBancaire compte);
    void modifierCompte(long id, boolean etat);
    boolean transfert(CompteBancaire compteSource,
                   CompteBancaire compteDest,
                   double somme);

    CompteBancaire getCompte(int id_client, String rib);
    List<CompteBancaire> getComptes(int id_client);
    List<CompteBancaire> getComptes();

}

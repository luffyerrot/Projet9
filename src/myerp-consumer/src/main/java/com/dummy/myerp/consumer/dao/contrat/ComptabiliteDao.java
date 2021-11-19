package com.dummy.myerp.consumer.dao.contrat;

import java.util.List;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Interface de DAO des objets du package Comptabilite
 */
public interface ComptabiliteDao {

    /**
     * Renvoie la liste des Comptes Comptables
     * @return {@link List}
     */
    List<CompteComptable> getListCompteComptable();

    /**
     * Renvoie la liste des Journaux Comptables
     * @return {@link List}
     */
    List<JournalComptable> getListJournalComptable();

    /**
     * Renvoie le Journal Comptable avec le code
     * @return {@link List}
     * @throws NotFoundException 
     */
    JournalComptable getJournalComptableByCode(String pCode) throws NotFoundException;


    // ==================== EcritureComptable ====================

    /**
     * Renvoie la liste des Écritures Comptables
     * @return {@link List}
     */
    List<EcritureComptable> getListEcritureComptable();

    /**
     * Renvoie la liste des Sequences d'écritures Comptables
     * @return {@link List}
     */
    List<SequenceEcritureComptable> getListSequenceEcritureComptable();

    /**
     * Renvoie l'Écriture Comptable d'id {@code pId}.
     *
     * @param pId l'id de l'écriture comptable
     * @return {@link EcritureComptable}
     * @throws NotFoundException : Si l'écriture comptable n'est pas trouvée
     */
    EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException;

    /**
     * Renvoie l'Écriture Comptable de référence {@code pRef}.
     *
     * @param pReference la référence de l'écriture comptable
     * @return {@link EcritureComptable}
     * @throws NotFoundException : Si l'écriture comptable n'est pas trouvée
     */
    EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException;

    /**
     * Renvoie la Sequence d'écriture Comptable avec l'année et le code du journal {@code pCodeJournal, pAnnee}.
     *
     * @param pAnnee l'année l'écriture comptable
     * @param pCodeJournal le code journal de l'écriture comptable
     * @return {@link SequenceEcritureComptable}
     * @throws NotFoundException : Si la sequence de l'écriture comptable n'est pas trouvée
     */
    SequenceEcritureComptable getSequenceEcritureComptableByJournalCodeAndYear(String pCodeJournal, Integer pAnnee) throws NotFoundException;

    /**
     * Charge la liste des lignes d'écriture de l'écriture comptable {@code pEcritureComptable}
     *
     * @param pEcritureComptable -
     */
    void loadListLigneEcriture(EcritureComptable pEcritureComptable);

    /**
     * Insert une nouvelle écriture comptable.
     *
     * @param pEcritureComptable -
     */
    void insertEcritureComptable(EcritureComptable pEcritureComptable);

    /**
     * Insert une nouvelle sequence d'écriture comptable.
     *
     * @param pSequenceEcritureComptable -
     */
    void insertSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable);

    /**
     * Met à jour l'écriture comptable.
     *
     * @param pEcritureComptable -
     */
    void updateEcritureComptable(EcritureComptable pEcritureComptable);

    /**
     * Met à jour la sequence de l'écriture comptable.
     *
     * @param pSequenceEcritureComptable -
     */
    void updateSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable);

    /**
     * Supprime l'écriture comptable d'id {@code pId}.
     *
     * @param pId l'id de l'écriture
     */
    void deleteEcritureComptable(Integer pId);
}

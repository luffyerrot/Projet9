package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    
    public EcritureComptable getEcritureComptable(Integer id) throws NotFoundException {
        return getDaoProxy().getComptabiliteDao().getEcritureComptable(id);
    }
    
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }
    
	@Override
	public JournalComptable getJournalComptableByCode(String pCode) {
		try {
			return getDaoProxy().getComptabiliteDao().getJournalComptableByCode(pCode);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

	@Override
	public List<SequenceEcritureComptable> getListSequenceEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListSequenceEcritureComptable();
	}

	@Override
	public SequenceEcritureComptable getSequenceEcritureComptableByJournalCodeAndYear(String journal_code, Integer annee) {
        try {
			return getDaoProxy().getComptabiliteDao().getSequenceEcritureComptableByJournalCodeAndYear(journal_code, annee);
		} catch (NotFoundException e) {
			return null;
		}
	}

    /**
     * {@inheritDoc}
     * @throws NotFoundException 
     */
    // TODO ?? tester
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        // TODO ?? impl??menter
        // Bien se r??ferer ?? la JavaDoc de cette m??thode !
        /* Le principe :
                1.  Remonter depuis la persitance la derni??re valeur de la s??quence du journal pour l'ann??e de l'??criture
                    (table sequence_ecriture_comptable)
                2.  * S'il n'y a aucun enregistrement pour le journal pour l'ann??e concern??e :
                        1. Utiliser le num??ro 1.
                    * Sinon :
                        1. Utiliser la derni??re valeur + 1
                3.  Mettre ?? jour la r??f??rence de l'??criture avec la r??f??rence calcul??e (RG_Compta_5)
                4.  Enregistrer (insert/update) la valeur de la s??quence en persitance
                    (table sequence_ecriture_comptable)
         */
    	if (pEcritureComptable != null) {
        	SequenceEcritureComptable lSequenceEcritureComptable = getSequenceEcritureComptableByJournalCodeAndYear(pEcritureComptable.getJournal().getCode(), 1900 + pEcritureComptable.getDate().getYear());
        	String number = null;
        	try {
	        	if (lSequenceEcritureComptable != null) {
	        		lSequenceEcritureComptable.setDerniereValeur(lSequenceEcritureComptable.getDerniereValeur() + 1);
	        		number = String.format("%05d", lSequenceEcritureComptable.getDerniereValeur());
					this.updateSequenceEcritureComptable(lSequenceEcritureComptable);
	        	} else {
	        		lSequenceEcritureComptable = new SequenceEcritureComptable(1900 + pEcritureComptable.getDate().getYear(), 1);
	        		lSequenceEcritureComptable.setJournal(this.getJournalComptableByCode(pEcritureComptable.getJournal().getCode()));
	        		number = String.format("%05d", lSequenceEcritureComptable.getDerniereValeur());
					this.insertSequenceEcritureComptable(lSequenceEcritureComptable);
	        	}
	        	
        	} catch (FunctionalException e) {
        		e.printStackTrace();
        	}
        	
        	StringBuilder builder = new StringBuilder();
        	builder.append(pEcritureComptable.getJournal().getCode())
        		.append("-")
        		.append(1900 + pEcritureComptable.getDate().getYear())
        		.append("/")
        		.append(number);
        	String reference = builder.toString();
        	pEcritureComptable.setReference(reference);
        	/*try {
            	if (pEcritureComptable.getId() != null) {
            		this.updateEcritureComptable(pEcritureComptable);
	        	} else {
	        		System.out.println(pEcritureComptable.toString());
					this.insertEcritureComptable(pEcritureComptable);
	        	}
			} catch (FunctionalException e) {
				e.printStackTrace();
			}*/
    	}
    }

    /**
     * {@inheritDoc}
     */
    // TODO ?? tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * V??rifie que l'Ecriture comptable respecte les r??gles de gestion unitaires,
     * c'est ?? dire ind??pendemment du contexte (unicit?? de la r??f??rence, exercie comptable non clotur??...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les r??gles de gestion
     */
    // TODO tests ?? compl??ter
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== V??rification des contraintes unitaires sur les attributs de l'??criture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'??criture comptable ne respecte pas les r??gles de gestion.",
                                          new ConstraintViolationException(
                                              "L'??criture comptable ne respecte pas les contraintes de validation",
                                              vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une ??criture comptable soit valide, elle doit ??tre ??quilibr??e
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'??criture comptable n'est pas ??quilibr??e.");
        }

        // ===== RG_Compta_3 : une ??criture comptable doit avoir au moins 2 lignes d'??criture (1 au d??bit, 1 au cr??dit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'??criture ?? une seule ligne
        //      avec un montant au d??bit et un montant au cr??dit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
            || vNbrCredit < 1
            || vNbrDebit < 1) {
            throw new FunctionalException(
                "L'??criture comptable doit avoir au moins deux lignes : une ligne au d??bit et une ligne au cr??dit.");
        }

        // TODO ===== RG_Compta_5 : Format et contenu de la r??f??rence
        // v??rifier que l'ann??e dans la r??f??rence correspond bien ?? la date de l'??criture, idem pour le code journal...
        String reference = pEcritureComptable.getReference();
        String annee = StringUtils.substringBeforeLast(StringUtils.substringAfterLast(reference, "-"), "/");
        String codeJournal = StringUtils.substringBeforeLast(reference, "-");
        if (!annee.equals(String.valueOf(1900 + pEcritureComptable.getDate().getYear()))) {
        	throw new FunctionalException(
                    "L'??criture comptable doit avoir la m??me ann??e que la r??f??rence.");
        }
        
        if (!codeJournal.equals(pEcritureComptable.getJournal().getCode())) {
        	throw new FunctionalException(
                    "L'??criture comptable doit avoir le m??me code journal que la r??f??rence.");
        	
        }
    }


    /**
     * V??rifie que l'Ecriture comptable respecte les r??gles de gestion li??es au contexte
     * (unicit?? de la r??f??rence, ann??e comptable non clotur??...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les r??gles de gestion
     */
    public void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La r??f??rence d'une ??criture comptable doit ??tre unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une ??criture ayant la m??me r??f??rence
            	try {
                    EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                            pEcritureComptable.getReference());
                    
                    // Si l'??criture ?? v??rifier est une nouvelle ??criture (id == null),
                    // ou si elle ne correspond pas ?? l'??criture trouv??e (id != idECRef),
                    // c'est qu'il y a d??j?? une autre ??criture avec la m??me r??f??rence
                    if (pEcritureComptable.getId() == null
                        || !pEcritureComptable.getId().equals(vECRef.getId())) {
                        throw new FunctionalException("Une autre ??criture comptable existe d??j?? avec la m??me r??f??rence.");
                    }
            	} catch (IncorrectResultSizeDataAccessException e) {
            		throw new FunctionalException("Plusieurs ??critures comptable on la m??me r??f??rence");
            	}
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ??a veut dire qu'on n'a aucune autre ??criture avec la m??me r??f??rence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }


	@Override
	public void insertSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable)
			throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
	}


	@Override
	public void updateSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable)
			throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}

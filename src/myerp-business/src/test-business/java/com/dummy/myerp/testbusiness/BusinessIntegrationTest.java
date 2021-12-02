package com.dummy.myerp.testbusiness;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/dummy/myerp/testbusiness/business/bootstrapContext.xml")
public class BusinessIntegrationTest extends BusinessTestCase {
	
    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    
	public BusinessIntegrationTest() {
		super();
	}
	
	@Test
	public void addReference() throws NotFoundException { 
		EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));
        manager.addReference(vEcritureComptable);
        
		EcritureComptable pEcritureComptable;
		pEcritureComptable = new EcritureComptable();
		pEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		pEcritureComptable.setDate(new Date());
		pEcritureComptable.setLibelle("Libelle2");
		pEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4456),
                null, new BigDecimal(123),
                null));
		pEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4457),
                null, null,
                new BigDecimal(123)));
        manager.addReference(pEcritureComptable);
	}
	
	@Test
	public void getListCompteComptable() {
		assertTrue(manager.getListCompteComptable() != null);
	}
	
	@Test
	public void getListEcritureComptable() {
		assertTrue(manager.getListEcritureComptable() != null);
	}
	
	@Test
	public void getListJournalComptable() {
		assertTrue(manager.getListJournalComptable() != null);
	}
	
	@Test
	public void getListSequenceEcritureComptable() {
		assertTrue(manager.getListSequenceEcritureComptable() != null);
	}
	
	@Test
	public void insertUpdateCheckAndDeleteEcritureComptable() throws FunctionalException, NotFoundException {
		EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));
        manager.addReference(vEcritureComptable);
        manager.insertEcritureComptable(vEcritureComptable);
        manager.checkEcritureComptable(vEcritureComptable);
        manager.getEcritureComptable(vEcritureComptable.getId());
        vEcritureComptable.setLibelle("LibelleTest");
        manager.updateEcritureComptable(vEcritureComptable);
        assertTrue(manager.getEcritureComptable(vEcritureComptable.getId()).getLibelle().equals(vEcritureComptable.getLibelle()));
        manager.deleteEcritureComptable(vEcritureComptable.getId());
	}
}

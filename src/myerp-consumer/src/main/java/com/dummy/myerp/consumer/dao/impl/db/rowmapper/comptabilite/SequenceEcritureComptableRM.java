package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dummy.myerp.consumer.dao.impl.cache.JournalComptableDaoCache;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;

/**
 * {@link RowMapper} de {@link SequenceEcritureComptable}
 */
public class SequenceEcritureComptableRM implements RowMapper<SequenceEcritureComptable> {

    /** JournalComptableDaoCache */
    private final JournalComptableDaoCache journalComptableDaoCache = new JournalComptableDaoCache();

    @Override
    public SequenceEcritureComptable mapRow(ResultSet pRS, int pRowNum) throws SQLException {
    	SequenceEcritureComptable vBean = new SequenceEcritureComptable();
        vBean.setAnnee(pRS.getInt("annee"));
        vBean.setDerniereValeur(pRS.getInt("derniere_valeur"));
        vBean.setJournal(journalComptableDaoCache.getByCode(pRS.getString("journal_code")));

        return vBean;
    }
}

/*
 * This file is generated by jOOQ.
 */
package org.example.dao.jooq.model.tables.records;


import org.example.dao.jooq.model.tables.Podcast;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PodcastRecord extends UpdatableRecordImpl<PodcastRecord> implements Record3<Short, JSONB, JSON> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>el_service.podcast.id</code>.
     */
    public void setId(Short value) {
        set(0, value);
    }

    /**
     * Getter for <code>el_service.podcast.id</code>.
     */
    public Short getId() {
        return (Short) get(0);
    }

    /**
     * Setter for <code>el_service.podcast.metadata</code>.
     */
    public void setMetadata(JSONB value) {
        set(1, value);
    }

    /**
     * Getter for <code>el_service.podcast.metadata</code>.
     */
    public JSONB getMetadata() {
        return (JSONB) get(1);
    }

    /**
     * Setter for <code>el_service.podcast.body</code>.
     */
    public void setBody(JSON value) {
        set(2, value);
    }

    /**
     * Getter for <code>el_service.podcast.body</code>.
     */
    public JSON getBody() {
        return (JSON) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Short> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Short, JSONB, JSON> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Short, JSONB, JSON> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Short> field1() {
        return Podcast.PODCAST.ID;
    }

    @Override
    public Field<JSONB> field2() {
        return Podcast.PODCAST.METADATA;
    }

    @Override
    public Field<JSON> field3() {
        return Podcast.PODCAST.BODY;
    }

    @Override
    public Short component1() {
        return getId();
    }

    @Override
    public JSONB component2() {
        return getMetadata();
    }

    @Override
    public JSON component3() {
        return getBody();
    }

    @Override
    public Short value1() {
        return getId();
    }

    @Override
    public JSONB value2() {
        return getMetadata();
    }

    @Override
    public JSON value3() {
        return getBody();
    }

    @Override
    public PodcastRecord value1(Short value) {
        setId(value);
        return this;
    }

    @Override
    public PodcastRecord value2(JSONB value) {
        setMetadata(value);
        return this;
    }

    @Override
    public PodcastRecord value3(JSON value) {
        setBody(value);
        return this;
    }

    @Override
    public PodcastRecord values(Short value1, JSONB value2, JSON value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PodcastRecord
     */
    public PodcastRecord() {
        super(Podcast.PODCAST);
    }

    /**
     * Create a detached, initialised PodcastRecord
     */
    public PodcastRecord(Short id, JSONB metadata, JSON body) {
        super(Podcast.PODCAST);

        setId(id);
        setMetadata(metadata);
        setBody(body);
    }
}

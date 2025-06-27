package com.mall.mbg.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OmsOrderReturnExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OmsOrderReturnExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(Long value) {
            addCriterion("order_id =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(Long value) {
            addCriterion("order_id <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(Long value) {
            addCriterion("order_id >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("order_id >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(Long value) {
            addCriterion("order_id <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("order_id <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<Long> values) {
            addCriterion("order_id in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<Long> values) {
            addCriterion("order_id not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(Long value1, Long value2) {
            addCriterion("order_id between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("order_id not between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdIsNull() {
            addCriterion("order_item_id is null");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdIsNotNull() {
            addCriterion("order_item_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdEqualTo(Long value) {
            addCriterion("order_item_id =", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdNotEqualTo(Long value) {
            addCriterion("order_item_id <>", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdGreaterThan(Long value) {
            addCriterion("order_item_id >", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdGreaterThanOrEqualTo(Long value) {
            addCriterion("order_item_id >=", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdLessThan(Long value) {
            addCriterion("order_item_id <", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdLessThanOrEqualTo(Long value) {
            addCriterion("order_item_id <=", value, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdIn(List<Long> values) {
            addCriterion("order_item_id in", values, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdNotIn(List<Long> values) {
            addCriterion("order_item_id not in", values, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdBetween(Long value1, Long value2) {
            addCriterion("order_item_id between", value1, value2, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andOrderItemIdNotBetween(Long value1, Long value2) {
            addCriterion("order_item_id not between", value1, value2, "orderItemId");
            return (Criteria) this;
        }

        public Criteria andReturnNoIsNull() {
            addCriterion("return_no is null");
            return (Criteria) this;
        }

        public Criteria andReturnNoIsNotNull() {
            addCriterion("return_no is not null");
            return (Criteria) this;
        }

        public Criteria andReturnNoEqualTo(String value) {
            addCriterion("return_no =", value, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoNotEqualTo(String value) {
            addCriterion("return_no <>", value, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoGreaterThan(String value) {
            addCriterion("return_no >", value, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoGreaterThanOrEqualTo(String value) {
            addCriterion("return_no >=", value, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoLessThan(String value) {
            addCriterion("return_no <", value, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoLessThanOrEqualTo(String value) {
            addCriterion("return_no <=", value, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoLike(String value) {
            addCriterion("return_no like", value, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoNotLike(String value) {
            addCriterion("return_no not like", value, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoIn(List<String> values) {
            addCriterion("return_no in", values, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoNotIn(List<String> values) {
            addCriterion("return_no not in", values, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoBetween(String value1, String value2) {
            addCriterion("return_no between", value1, value2, "returnNo");
            return (Criteria) this;
        }

        public Criteria andReturnNoNotBetween(String value1, String value2) {
            addCriterion("return_no not between", value1, value2, "returnNo");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIsNull() {
            addCriterion("apply_time is null");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIsNotNull() {
            addCriterion("apply_time is not null");
            return (Criteria) this;
        }

        public Criteria andApplyTimeEqualTo(Date value) {
            addCriterion("apply_time =", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotEqualTo(Date value) {
            addCriterion("apply_time <>", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeGreaterThan(Date value) {
            addCriterion("apply_time >", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("apply_time >=", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeLessThan(Date value) {
            addCriterion("apply_time <", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeLessThanOrEqualTo(Date value) {
            addCriterion("apply_time <=", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIn(List<Date> values) {
            addCriterion("apply_time in", values, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotIn(List<Date> values) {
            addCriterion("apply_time not in", values, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeBetween(Date value1, Date value2) {
            addCriterion("apply_time between", value1, value2, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotBetween(Date value1, Date value2) {
            addCriterion("apply_time not between", value1, value2, "applyTime");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdIsNull() {
            addCriterion("return_reason_id is null");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdIsNotNull() {
            addCriterion("return_reason_id is not null");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdEqualTo(Long value) {
            addCriterion("return_reason_id =", value, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdNotEqualTo(Long value) {
            addCriterion("return_reason_id <>", value, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdGreaterThan(Long value) {
            addCriterion("return_reason_id >", value, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdGreaterThanOrEqualTo(Long value) {
            addCriterion("return_reason_id >=", value, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdLessThan(Long value) {
            addCriterion("return_reason_id <", value, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdLessThanOrEqualTo(Long value) {
            addCriterion("return_reason_id <=", value, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdIn(List<Long> values) {
            addCriterion("return_reason_id in", values, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdNotIn(List<Long> values) {
            addCriterion("return_reason_id not in", values, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdBetween(Long value1, Long value2) {
            addCriterion("return_reason_id between", value1, value2, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andReturnReasonIdNotBetween(Long value1, Long value2) {
            addCriterion("return_reason_id not between", value1, value2, "returnReasonId");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsIsNull() {
            addCriterion("return_logistics is null");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsIsNotNull() {
            addCriterion("return_logistics is not null");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsEqualTo(String value) {
            addCriterion("return_logistics =", value, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNotEqualTo(String value) {
            addCriterion("return_logistics <>", value, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsGreaterThan(String value) {
            addCriterion("return_logistics >", value, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsGreaterThanOrEqualTo(String value) {
            addCriterion("return_logistics >=", value, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsLessThan(String value) {
            addCriterion("return_logistics <", value, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsLessThanOrEqualTo(String value) {
            addCriterion("return_logistics <=", value, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsLike(String value) {
            addCriterion("return_logistics like", value, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNotLike(String value) {
            addCriterion("return_logistics not like", value, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsIn(List<String> values) {
            addCriterion("return_logistics in", values, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNotIn(List<String> values) {
            addCriterion("return_logistics not in", values, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsBetween(String value1, String value2) {
            addCriterion("return_logistics between", value1, value2, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNotBetween(String value1, String value2) {
            addCriterion("return_logistics not between", value1, value2, "returnLogistics");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoIsNull() {
            addCriterion("return_logistics_no is null");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoIsNotNull() {
            addCriterion("return_logistics_no is not null");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoEqualTo(String value) {
            addCriterion("return_logistics_no =", value, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoNotEqualTo(String value) {
            addCriterion("return_logistics_no <>", value, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoGreaterThan(String value) {
            addCriterion("return_logistics_no >", value, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoGreaterThanOrEqualTo(String value) {
            addCriterion("return_logistics_no >=", value, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoLessThan(String value) {
            addCriterion("return_logistics_no <", value, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoLessThanOrEqualTo(String value) {
            addCriterion("return_logistics_no <=", value, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoLike(String value) {
            addCriterion("return_logistics_no like", value, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoNotLike(String value) {
            addCriterion("return_logistics_no not like", value, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoIn(List<String> values) {
            addCriterion("return_logistics_no in", values, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoNotIn(List<String> values) {
            addCriterion("return_logistics_no not in", values, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoBetween(String value1, String value2) {
            addCriterion("return_logistics_no between", value1, value2, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnLogisticsNoNotBetween(String value1, String value2) {
            addCriterion("return_logistics_no not between", value1, value2, "returnLogisticsNo");
            return (Criteria) this;
        }

        public Criteria andReturnProofIsNull() {
            addCriterion("return_proof is null");
            return (Criteria) this;
        }

        public Criteria andReturnProofIsNotNull() {
            addCriterion("return_proof is not null");
            return (Criteria) this;
        }

        public Criteria andReturnProofEqualTo(String value) {
            addCriterion("return_proof =", value, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofNotEqualTo(String value) {
            addCriterion("return_proof <>", value, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofGreaterThan(String value) {
            addCriterion("return_proof >", value, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofGreaterThanOrEqualTo(String value) {
            addCriterion("return_proof >=", value, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofLessThan(String value) {
            addCriterion("return_proof <", value, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofLessThanOrEqualTo(String value) {
            addCriterion("return_proof <=", value, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofLike(String value) {
            addCriterion("return_proof like", value, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofNotLike(String value) {
            addCriterion("return_proof not like", value, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofIn(List<String> values) {
            addCriterion("return_proof in", values, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofNotIn(List<String> values) {
            addCriterion("return_proof not in", values, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofBetween(String value1, String value2) {
            addCriterion("return_proof between", value1, value2, "returnProof");
            return (Criteria) this;
        }

        public Criteria andReturnProofNotBetween(String value1, String value2) {
            addCriterion("return_proof not between", value1, value2, "returnProof");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andReviewNotesIsNull() {
            addCriterion("review_notes is null");
            return (Criteria) this;
        }

        public Criteria andReviewNotesIsNotNull() {
            addCriterion("review_notes is not null");
            return (Criteria) this;
        }

        public Criteria andReviewNotesEqualTo(String value) {
            addCriterion("review_notes =", value, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesNotEqualTo(String value) {
            addCriterion("review_notes <>", value, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesGreaterThan(String value) {
            addCriterion("review_notes >", value, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesGreaterThanOrEqualTo(String value) {
            addCriterion("review_notes >=", value, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesLessThan(String value) {
            addCriterion("review_notes <", value, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesLessThanOrEqualTo(String value) {
            addCriterion("review_notes <=", value, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesLike(String value) {
            addCriterion("review_notes like", value, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesNotLike(String value) {
            addCriterion("review_notes not like", value, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesIn(List<String> values) {
            addCriterion("review_notes in", values, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesNotIn(List<String> values) {
            addCriterion("review_notes not in", values, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesBetween(String value1, String value2) {
            addCriterion("review_notes between", value1, value2, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewNotesNotBetween(String value1, String value2) {
            addCriterion("review_notes not between", value1, value2, "reviewNotes");
            return (Criteria) this;
        }

        public Criteria andReviewerIsNull() {
            addCriterion("reviewer is null");
            return (Criteria) this;
        }

        public Criteria andReviewerIsNotNull() {
            addCriterion("reviewer is not null");
            return (Criteria) this;
        }

        public Criteria andReviewerEqualTo(String value) {
            addCriterion("reviewer =", value, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerNotEqualTo(String value) {
            addCriterion("reviewer <>", value, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerGreaterThan(String value) {
            addCriterion("reviewer >", value, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerGreaterThanOrEqualTo(String value) {
            addCriterion("reviewer >=", value, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerLessThan(String value) {
            addCriterion("reviewer <", value, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerLessThanOrEqualTo(String value) {
            addCriterion("reviewer <=", value, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerLike(String value) {
            addCriterion("reviewer like", value, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerNotLike(String value) {
            addCriterion("reviewer not like", value, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerIn(List<String> values) {
            addCriterion("reviewer in", values, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerNotIn(List<String> values) {
            addCriterion("reviewer not in", values, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerBetween(String value1, String value2) {
            addCriterion("reviewer between", value1, value2, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewerNotBetween(String value1, String value2) {
            addCriterion("reviewer not between", value1, value2, "reviewer");
            return (Criteria) this;
        }

        public Criteria andReviewTimeIsNull() {
            addCriterion("review_time is null");
            return (Criteria) this;
        }

        public Criteria andReviewTimeIsNotNull() {
            addCriterion("review_time is not null");
            return (Criteria) this;
        }

        public Criteria andReviewTimeEqualTo(Date value) {
            addCriterion("review_time =", value, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeNotEqualTo(Date value) {
            addCriterion("review_time <>", value, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeGreaterThan(Date value) {
            addCriterion("review_time >", value, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("review_time >=", value, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeLessThan(Date value) {
            addCriterion("review_time <", value, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeLessThanOrEqualTo(Date value) {
            addCriterion("review_time <=", value, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeIn(List<Date> values) {
            addCriterion("review_time in", values, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeNotIn(List<Date> values) {
            addCriterion("review_time not in", values, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeBetween(Date value1, Date value2) {
            addCriterion("review_time between", value1, value2, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReviewTimeNotBetween(Date value1, Date value2) {
            addCriterion("review_time not between", value1, value2, "reviewTime");
            return (Criteria) this;
        }

        public Criteria andReturnAmountIsNull() {
            addCriterion("return_amount is null");
            return (Criteria) this;
        }

        public Criteria andReturnAmountIsNotNull() {
            addCriterion("return_amount is not null");
            return (Criteria) this;
        }

        public Criteria andReturnAmountEqualTo(BigDecimal value) {
            addCriterion("return_amount =", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountNotEqualTo(BigDecimal value) {
            addCriterion("return_amount <>", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountGreaterThan(BigDecimal value) {
            addCriterion("return_amount >", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("return_amount >=", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountLessThan(BigDecimal value) {
            addCriterion("return_amount <", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("return_amount <=", value, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountIn(List<BigDecimal> values) {
            addCriterion("return_amount in", values, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountNotIn(List<BigDecimal> values) {
            addCriterion("return_amount not in", values, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_amount between", value1, value2, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andReturnAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("return_amount not between", value1, value2, "returnAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountIsNull() {
            addCriterion("deduction_amount is null");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountIsNotNull() {
            addCriterion("deduction_amount is not null");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountEqualTo(BigDecimal value) {
            addCriterion("deduction_amount =", value, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountNotEqualTo(BigDecimal value) {
            addCriterion("deduction_amount <>", value, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountGreaterThan(BigDecimal value) {
            addCriterion("deduction_amount >", value, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("deduction_amount >=", value, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountLessThan(BigDecimal value) {
            addCriterion("deduction_amount <", value, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("deduction_amount <=", value, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountIn(List<BigDecimal> values) {
            addCriterion("deduction_amount in", values, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountNotIn(List<BigDecimal> values) {
            addCriterion("deduction_amount not in", values, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("deduction_amount between", value1, value2, "deductionAmount");
            return (Criteria) this;
        }

        public Criteria andDeductionAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("deduction_amount not between", value1, value2, "deductionAmount");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
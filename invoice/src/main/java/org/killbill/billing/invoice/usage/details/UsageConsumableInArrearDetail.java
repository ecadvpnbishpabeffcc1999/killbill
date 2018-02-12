/*
 * Copyright 2014-2018 Groupon, Inc
 * Copyright 2014-2018 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.invoice.usage.details;

import java.math.BigDecimal;
import java.util.List;

import org.killbill.billing.util.jackson.ObjectMapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;

public class UsageConsumableInArrearDetail implements UsageInArrearDetail {

    private final List<UsageConsumableInArrearTierUnitDetail> tierDetails;
    private BigDecimal amount;

    public UsageConsumableInArrearDetail(@JsonProperty("tierDetails") List<UsageConsumableInArrearTierUnitDetail> tierDetails) {
        this(tierDetails, computeAmount(tierDetails));
    }

    @JsonCreator
    public UsageConsumableInArrearDetail(@JsonProperty("tierDetails") List<UsageConsumableInArrearTierUnitDetail> tierDetails,
                                         @JsonProperty("amount") BigDecimal amount) {
        this.tierDetails = tierDetails;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toJson(final ObjectMapper objectMapper) {
        String result = null;
        if (tierDetails != null && tierDetails.size() > 0){
            try {
                result = objectMapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                Preconditions.checkState(false, e.getMessage());
            }
        }
        return result;
    }

    public List<UsageConsumableInArrearTierUnitDetail> getTierDetails() {
        return tierDetails;
    }

    private static BigDecimal computeAmount(final List<UsageConsumableInArrearTierUnitDetail> tierDetails) {
        BigDecimal result = BigDecimal.ZERO;
        for (UsageConsumableInArrearTierUnitDetail toBeBilled : tierDetails) {
            result = result.add(toBeBilled.getAmount());
        }
        return result;
    }
}
package com.project.pill_so_good.pill.policy;

public class PillPolicyFactory {

    public static PillPolicy getPillPolicyInstance(String DBKey) {
        if (DBKey.equals("AGE")) {
            return new AgePillPolicy();
        } else if (DBKey.equals("PREGNANT_WOMAN")) {
            return new PregnantWomanPillPolicy();
        } else if (DBKey.equals("MULTIPLE_MEDICINE")) {
            return new MultiplePillPolicy();
        }
        return null; // 나중에 수정
    }
}

package sample.model;

import java.util.Objects;

public class Flag
{
    private int flag_id;
    private String nation;

    public Flag(int flag_id, String nation) {
        this.flag_id = flag_id;
        this.nation = nation;
    }

    public String getNation() {
        return nation;
    }

    public int getFlag_id() {
        return flag_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flag flag = (Flag) o;
        return flag_id == flag.flag_id &&
                Objects.equals(nation, flag.nation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flag_id, nation);
    }
}

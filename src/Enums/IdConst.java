package Enums;

public enum IdConst
{
    SIN_ID(-1);

    private final Integer value;

    IdConst(Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return this.value;
    }
}

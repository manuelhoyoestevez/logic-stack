package com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper;

import com.mhe.dev.logic.stack.core.logic.model.TruthTable;
import com.mhe.dev.logic.stack.core.logic.model.TruthTableImpl;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.TruthTableDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruthTableMapper
{
    public TruthTableDto toDto(TruthTable truthTable)
    {
        if (truthTable == null)
        {
            return null;
        }

        return new TruthTableDto()
            .literals(truthTable.getLiterals())
            .values(truthTable.getValues());
    }

    private Map<Integer, Boolean> listToMap(List<Boolean> values)
    {
        if (values == null)
        {
            return null;
        }

        Map<Integer, Boolean> map = new HashMap<>();

        for (int i = 0; i < values.size(); i++)
        {
            map.put(i, values.get(i));
        }

        return map;
    }

    public TruthTable fromDto(TruthTableDto dto)
    {
        if (dto == null)
        {
            return null;
        }

        return new TruthTableImpl(dto.getLiterals(), listToMap(dto.getValues()));
    }
}

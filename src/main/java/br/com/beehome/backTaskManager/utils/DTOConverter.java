package br.com.beehome.backTaskManager.utils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DTOConverter {
    public <Class, DTO> List<Class> convertOptionalDTOListToClassList(Optional<List<DTO>> optionalDTOList, Function<DTO, Class> converter) {
        return optionalDTOList.map(dtoList -> dtoList.stream()
                        .map(converter)
                        .collect(Collectors.toList()))
                .orElse(List.of()); // Returns empty list if Optional is empty
    }
}


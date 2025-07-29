### 3.2 Note di sviluppo

- **Generics avanzati**  
  - **Permalink:** (https://github.com/CarusoMatteo/RisikOOP/blob/5033629c3594c141715ed421ee7aa94e77a9c043/src/main/java/it/unibo/risikoop/model/interfaces/Specification.java#L11) 
  - **Descrizione:** definizione dell’interfaccia parametrizzata `Specification<T>` con default methods `and()`, `or()`, `not()` e creazione di implementazioni concrete (es. `KillPlayerOrConquer24TerritoriesSpec`) per massimizzare il riuso della logica di verifica.

- **Lambda expressions**  
  - **Permalink:** (https://github.com/CarusoMatteo/RisikOOP/blob/5033629c3594c141715ed421ee7aa94e77a9c043/src/main/java/it/unibo/risikoop/model/interfaces/Specification.java#L38C5-L40C6) 
  - **Descrizione:** uso di espressioni lambda nei default methods di `Specification<T>` (es. `c -> this.isSatisfiedBy(c) && other.isSatisfiedBy(c)`) per definire operatori logici in modo conciso e inline.

- **Stream API**  
  - **Permalink:** 
  - (https://github.com/CarusoMatteo/RisikOOP/blob/5033629c3594c141715ed421ee7aa94e77a9c043/src/main/java/it/unibo/risikoop/model/implementations/TurnManagerImpl.java#L49C9-L50C58) 
  - (https://github.com/CarusoMatteo/RisikOOP/blob/5033629c3594c141715ed421ee7aa94e77a9c043/src/main/java/it/unibo/risikoop/model/implementations/gamecards/objectivecards/ConquerNContinetsBuilder.java#L42C9-L47C31)
  - 
  - **Descrizione:** impiego di pipeline Stream per snellire operazioni iterative, ad esempio:  
    - `players.stream().filter(p -> !p.isEliminated()).toList()`  
    - `continents.stream().map(Continent::getName).collect(Collectors.joining(", ", "Conquer all: ", "."))`  


- **Reflection (Class\<T>)**  
  - **Permalink:** 
  - (https://github.com/CarusoMatteo/RisikOOP/blob/5033629c3594c141715ed421ee7aa94e77a9c043/src/main/java/it/unibo/risikoop/controller/implementations/GamePhaseControllerImpl.java#L235C5-L240C6) 
  - **Descrizione:** utilizzo di `Class<T>.isInstance()` e `Class.cast()` per implementare in un unico punto un cast sicuro e dinamico delle fasi di gioco, riducendo i controlli `instanceof` sparsi.

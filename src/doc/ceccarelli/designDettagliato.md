### 2.2.1 Gestione del “mazzo” di carte obiettivo
**Problema** Non essendo la mappa fissa, è necessario realizzare delle carte obbiettivo che si adattassero dinamicamente alla configurazione della mappa selezioanta.

**Soluzione**
**Template method** creo una classe astratta `AbstractObjectiveCardBuilder` che incorpora la logica di creazione delle carte espondo i metodo astratti primitivi `buildDescription` e `buildSpecification`,  che genera la carta mediante il metodo template `createCard`. Ho preferito l'utilizzo del **template method** rispetto al **pattern startegi** perchè mi permetteva meglio di isolare nelle classi che ereditano la `AbstractObjectiveCardBuilder` unicamente gli elementi che differiscono tra i le varie tipologie, incorparando gli aspetti comuni legati alla loro creazione.

### 2.2.2 Composizione delle condizioni di vittoria con Specification Pattern
#### Problema
Avere un modo semplice, espressiovo e componibile per definire e controllare la condizzione di vittoria delle carte obbiettivo, che spesso condividono aspetti di logiche comuni, come ad esempio conquinsta X territorio, o conquista almeno N territori con almeno M truppe.

#### Soluzione 
##### Specification Pattern
Lo specification pattern mi permette di rispondere a questa esegenza e, mediante la combinazione logica di tante piccole condizioni atomiche, mi consente di realizzare espressioni logiche più o meno complesse. La parte centrale del pattern risiede nell'interfaccia funzionale
```java
@FunctionalInterface
public interface Specification<T> {
    boolean isSatisfiedBy(T candidate);

    default Specification<T> and(Specification<T> other) { … }
    default Specification<T> or(Specification<T> other) { … }
    default Specification<T> not() { … }
}
```
che espone i metodi and, or, not repsonsabile della composizione delle varie specifiche.
I vantaggi introdotti da questo pattern mi hanno portato a preferire questa soluzione, all' alternative di Strategy che non si presatava al riutilizzo di logiche comuni, come ad esempio conquista N territori.  
La scelta dello Specification Pattern si è rivelata dunque la più adatta per astrarre la logica di verifica dell’obiettivo dalla costruzione della carta, massimizzando il riuso e mantenendo il design pulito e modulare. 


### 2.2.3 Gestione delle fasi di gioco
#### Problema  
RisikOOP è un gioco a turni in cui, in base alla fase corrente, gli stessi eventi della GUI (ad es. “selectTerritory” o “performAction”) devono produrre comportamenti diversi.  

#### Soluzione  
##### Strategy Pattern  
Si è adottato il **pattern Strategy**, modellando ogni fase di gioco come una strategia indipendente, aderente all'interfaccia base
```java
   public interface GamePhase {
     boolean selectTerritory(Territory t);
     boolean isComplete();
   }
```
In Più dato che non tutti gli eventi interessano ogni fase del gioco si è scelto di creare tante piccole interfacce ogniuna responsabile di incapsulare la logica di un'azione specifica, così facendo ogni fase implentava unicamnete le interfacce delle azzioni corrispondenti.






### 3.2 Note di sviluppo

- **Generics avanzati**  
  - **Permalink:** _da inserire_  
  - **Descrizione:** definizione dell’interfaccia parametrizzata `Specification<T>` con default methods `and()`, `or()`, `not()` e creazione di implementazioni concrete (es. `KillPlayerOrConquer24TerritoriesSpec`) per massimizzare il riuso della logica di verifica.

- **Lambda expressions**  
  - **Permalink:** _da inserire_  
  - **Descrizione:** uso di espressioni lambda nei default methods di `Specification<T>` (es. `c -> this.isSatisfiedBy(c) && other.isSatisfiedBy(c)`) per definire operatori logici in modo conciso e inline.

- **Stream API**  
  - **Permalink:** _da inserire_  
  - **Descrizione:** impiego di pipeline Stream per snellire operazioni iterative, ad esempio:  
    - `players.stream().filter(p -> !p.isEliminated()).toList()`  
    - `continents.stream().map(Continent::getName).collect(Collectors.joining(", ", "Conquer all: ", "."))`  
    - `flatMap(cont -> cont.getTerritories().stream()).collect(Collectors.toSet())`

- **Optional**  
  - **Permalink:** _da inserire_  
  - **Descrizione:** uso di `Optional<T>` nel metodo generico `currentPhaseAs(Class<T> iface)` per evitare null checks e restituire in modo esplicito l’eventuale presenza di un’istanza compatibile.

- **Reflection (Class\<T>)**  
  - **Permalink:** _da inserire_  
  - **Descrizione:** utilizzo di `Class<T>.isInstance()` e `Class.cast()` per implementare in un unico punto un cast sicuro e dinamico delle fasi di gioco, riducendo i controlli `instanceof` sparsi.


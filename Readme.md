# Läbimängija

Algoritmide ja andmestruktuuride ainele loodud rakendus läbimängimiseks ning harjutamiseks.

## Nõuded programmi käivitamiseks

Programmi käivitamiseks on vaja:

- `JDK 21`, mis on kättesaadav aadressilt: https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html 
- `Gradle Wrapperit` eraldi paigaldama ei pea, see on projektis olemas

Tähtis:

- Projekt töötab `Java 21` versiooniga,
- kui programm annab käivitamisel vea stiilis `compiled by a more recent version of the Java Runtime`, siis on aktiivne Java versioon liiga vana.

Java versiooni kontroll käsuviibal:

```cmd
java -version
```

Oodatud vastus on Java 21.

## Projekti allalaadimine

Kui kasutad Git-i:

```cmd
git clone https://github.com/P2-AA/Labimangija.git
cd Labimangija
```

Kui projekt on juba arvutisse kloonitud, ava käsuviip projekti juurkaustas.

## Programmi käivitamine

### Windows


Käsuviibal tuleb projekti juurkaustas olles sisestada:
```cmd
.\gradlew.bat run
```

Linux'i või macOS'i puhul:
```bash
./gradlew run
```

See käst käivitab läbimängija rakenduse.

## Käivitatava rakenduse jagatava kausta loomine

Projekt on seadistatud `jlink'i` jaoks. See võimaldab luua käivitatava rakenduse kokkupakitud kujul, koos vajalike Java käigufailidega.

### Windows


Projekti juurkaustas tuleb käsuviibal sisestada:
```cmd
.\gradlew.bat jlinkZip
```

Juhul kui programmikoodis on midagi muudetud või projektist on juba `jlinkZip` tehtud tuleks käsuviibal sisestada:
```cmd
.\gradlew.bat clean jlinkZip
```

Pärast eelmainitud käskluseid tekib projekti juurkausta kokkupakitud kujul, käivitatav programm:

```text
build/distributions/*.zip
```

Rakendust saab käivitada, kui pakkida lahti tekitatud fail ning käivitada järgnev jooksutatav fail:
```text
../image/bin/app.bat
```

## Sisendfailid ja logid

- Sisendfailid asuvad kaustas `sisendid/`,
- `jlinkZip` pakib olemasolevad `sisendid` rakendusega kaasa,
- rakendus loob vajadusel kausta `labimangud`, kus hoitakse logisid.

Kui kasutaja valib ülesande jaoks faili, loetakse see projektis olevatest sisendikaustadest või kasutaja vabalt valitud failist.

## Soovitus IntelliJ kasutajale

Kui avad projekti IntelliJ IDEA-s:

1. seadista projekti SDK-ks `JDK 21`,
2. lae Gradle projekt uuesti,
3. käivita projekt Gradle käsuga `run` või klassist `MainApp`

## Tüüpilised probleemid

### JavaFX ei käivitu

Põhjus:

- Tavaliselt on probleem vales Java versioonis või selles, et projekt pole Gradle poolt ehitatud/käivitatud

Kasulikud käsud:

```powershell
.\gradlew.bat run
.\gradlew.bat compileJava
```

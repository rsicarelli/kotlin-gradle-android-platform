# kotlin-gradle-android-plugins

Artigo original: [**Parte 0: Introdução a série**](https://dev.to/rsicarelli/android-plataforma-parte-0-introducao-a-serie-1ffe)

No mundo do desenvolvimento Android, enfrentamos um fluxo contínuo de novidades: novos dispositivos, atualizações do SDK e uma variedade de bibliotecas e ferramentas que são lançadas todos os dias.

Junte a isso as constantes alterações, depreciações e a pressão do negócio para entregar funcionalidades que atendam às necessidades dos clientes. Este cenário volátil torna-se um desafio para manter nosso código organizado, eficiente e escalável.

Nesta série de artigos, guiarei vocês através do processo de criação de uma Plataforma Android.

## O que é uma plataforma?

Ao escalar um projeto Android, encontramos desafios como modularização e gestão de dependências. A necessidade de performance, otimização, consistência UI/UX e garantir a retrocompatibilidade também se tornam cada vez mais urgentes.

Uma "plataforma" é um conjunto coeso e bem definido de práticas, ferramentas e bibliotecas que agilizam o desenvolvimento.

Ela se destaca como uma solução robusta e escalável para enfrentar os desafios inerentes ao desenvolvimento Android, auxiliando em aspectos como modularização, otimização, integração contínua, testes e segurança.

## Construindo uma Plataforma no Android
No decorrer desses artigos, iremos construir uma plataforma desde o início, acoplando-a a um projeto já existente.

🔗 [github.com/rsicarelli/kotlin-gradle-android-platform](https://github.com/rsicarelli/kotlin-gradle-android-platform)

1. [**Modularização**](https://dev.to/rsicarelli/android-plataforma-parte-1-modularizacao-2016)
  - Exploraremos as razões e ideias por trás da modularização no Android.
2. [**Início do Projeto**](https://dev.to/rsicarelli/android-plataforma-parte-2-inicio-do-projeto-34jg)
  - Apresentaremos o projeto base que será evoluído e integrado com nossa plataforma.
3. [**Compartilhando scripts do Gradle**](https://dev.to/rsicarelli/android-plataforma-parte-3-compartilhando-scripts-do-gradle-5ak3)
  - Abordaremos o `buildSrc` e discutiremos como compartilhar scripts em projetos Gradle.
4. [**Composite Build**](https://dev.to/rsicarelli/android-plataforma-parte-4-composite-build-lgk)
  - Introduziremos essa valiosa feature do Gradle e criaremos um composite build para representar nossa plataforma.
5. [**Simplificando Gradle Init**](https://dev.to/rsicarelli/android-plataforma-parte-5-simplificando-gradle-init-1b55)
  - Abordaremos, com mais contexto, as funcionalidades realmente necessárias para nossa plataforma.
6. [**Version Catalog**](https://dev.to/rsicarelli/android-plataforma-parte-6-version-catalog-59ob)
  - Introduziremos e implementaremos o catálogo de versões do Gradle.
7. [**Decorando o módulo 'app'**](https://dev.to/rsicarelli/android-plataforma-parte-7-decorando-o-modulo-app-2ah4)
  - Introduziremos o conceito de "decorações" e delegaremos os scripts Gradle do `app` para nossa plataforma.
8. [**Decorando os módulos 'library'**](https://dev.to/rsicarelli/android-plataforma-parte-8-decorando-os-modulo-library-4mm0)
  - Prosseguiremos para nossas libraries e também delegaremos a lógica para nossa plataforma.
9. [**Unificando a Application e Library extensions com a Common Extension**](https://dev.to/rsicarelli/android-plataforma-parte-9-unificando-a-application-e-library-extensions-com-a-common-extension-19gc)
  - Discutiremos sobre `ApplicationExtension`, `LibraryExtension` e `CommonsExtension` do Android Gradle Plugin (AGP).
10. [**Customização dos módulos**](https://dev.to/rsicarelli/android-plataforma-parte-10-customizacao-dos-modulos-2a7)
- Parametrizaremos nossa plataforma, possibilitando customizações nos módulos que a utilizam.
11. [**Criando uma DSL para customizar as novas opções**](https://dev.to/rsicarelli/android-plataforma-parte-11-criando-uma-dsl-para-customizar-as-novas-opcoes-1m1e)
- Implementaremos uma forma idiomática em Kotlin para realizar nossas customizações com a plataforma.
12. [**Otimizando tempo de compilação para bibliotecas Android**](https://dev.to/rsicarelli/android-plataforma-parte-12-otimizando-tempo-de-compilacao-para-bibliotecas-android-3g36)
- Discutiremos sobre as `BuildFeatures` do Android Gradle Plugin e otimizaremos a compilação de bibliotecas Android.
13. [**Incluindo módulos "puro JVM"**](https://dev.to/rsicarelli/android-plataforma-parte-13-incluindo-modulos-puro-jvm-4f61)
- Exploraremos os motivos para ter módulos mais "enxutos" e evitar etapas extras do Android Gradle Plugin.
14. [**Aderindo a funcionalidades experimentais do compilador do Kotlin**](https://dev.to/rsicarelli/android-plataforma-parte-14-aderindo-a-funcionalidades-experimentais-do-compilador-do-kotlin-3b0g)
- Discutiremos sobre a anotação `@RequiresOptIn` e como adaptar nossa plataforma para aderir a funcionalidades experimentais.
15. [**Cuidando do código com Detekt, Klint e Spotless**](https://dev.to/rsicarelli/android-plataforma-parte-15-cuidando-do-codigo-com-detekt-klint-e-spotless-50n7)
- Focaremos na qualidade de código, utilizando nossa plataforma para adaptar nosso projeto com ferramentas de análise e formatação de código.
16. [**Considerações finais**](https://dev.to/rsicarelli/android-plataforma-parte-16-consideracoes-finais-53f2)
- Refletiremos sobre a real necessidade de uma plataforma, boas práticas e os próximos passos para futuras séries!

![Image description](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/sjeq93uk3vdzdp6sv0ja.png)

## Spoiler do que iremos aprender

Iremos constrúir algumas decorações para serem aplicadas em nossos `build.gradle.kts`:
- Declarando um módulo `app` através da função `androidApp()`
- Declarando módulos `library` através da função `androidLibrary()`
- Declarando módulos puramente JVM através da função `jvmLibrary()`
- Integração com `detekt` e `spotless` como ferramentas de qulidade de código.

```kotlin
import com.rsicarelli.kplatform.detekt
import com.rsicarelli.kplatform.spotless

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.arturbosch.detekt) apply false
    alias(libs.plugins.diffplug.spotless) apply false
    id(libs.plugins.rsicarelli.kplatform.get().pluginId)
}

detekt()
spotless()
```
```kotlin
import com.rsicarelli.kplatform.androidApp

plugins {
    id(libs.plugins.android.application.get().pluginId)
    kotlin("android")
}

androidApp {
    versionCode = 1
    versionName = "1.0.0"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(projects.core.designsystem)
    implementation(projects.features.home)
}
```
```kotlin
import com.rsicarelli.kplatform.androidLibrary

plugins {
    id(libs.plugins.android.library.get().pluginId)
    kotlin("android")
}

androidLibrary()

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
}
```
```kotlin
import com.rsicarelli.kplatform.jvmLibrary

plugins {
    kotlin("jvm")
}

jvmLibrary()

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
```

## Bons estudos!
Espero que desfrute desse conteúdo e possa aprender bastante!

Se tiver qualquer dúvida ou problema, não hesite em me contatar, ou deixar um comentário.

Comece por aqui: [**Parte 1: Modularização**](https://dev.to/rsicarelli/android-plataforma-parte-1-modularizacao-2016)

## License

```
Copyright 2023 Rodrigo Sicarelli

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

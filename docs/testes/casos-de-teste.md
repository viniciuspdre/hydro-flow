INSTITUTO FEDERAL DE PERNAMBUCO CAMPUS BELO JARDIM

CAMILLE MARIA DIAS DE BARROS SILVA

GABRIEL CAMELO

IGOR NAYAN

LUCAS CAVALCANTE DA SILVA

PEDRO VINICIUS SILVA LIRA

DOCUMENTAÇÃO DE CASOS DE TESTE - Mais água para nosso povo!

BELO JARDIM, PE

2026

<div style="page-break-after: always;"></div>

Módulo de Autenticação

1. Login com Primeiro Acesso
   ● ID do Caso de Teste: CT-001
   ● Nome: Teste de Login com Primeiro Acesso
   ● Objetivo: Garantir que o sistema identifique o primeiro acesso do usuário, bloqueie a entrada e solicite a troca de senha antes de prosseguir.
   ● Pré-condições: O usuário está cadastrado no sistema e ainda não realizou a troca de senha inicial.
   ● Entradas:
     1. E-mail: "maria@example.com"
     2. Senha: "senha1234"
   ● Passos para Execução:
     1. Acesse a tela de login do sistema.
     2. Informe o e-mail e a senha do usuário.
     3. Solicite o acesso ao sistema.
     4. Verifique a resposta exibida pelo sistema.
   ● Resultado Esperado: O sistema deve bloquear o acesso, exibir a mensagem "Troque sua senha antes de continuar" e não iniciar a sessão do usuário.
   ● Critérios de Sucesso: O acesso é bloqueado, a mensagem correta é exibida e nenhuma sessão é criada.
   ● Critérios de Falha: O sistema permite o acesso, não exibe a mensagem correta ou cria uma sessão indevidamente.

Módulo de Usuários

1. Listagem de Todos os Usuários
   ● ID do Caso de Teste: CT-002
   ● Nome: Teste de Listagem de Todos os Usuários
   ● Objetivo: Garantir que a listagem de usuários exiba todos os cadastrados no sistema, sem expor as senhas.
   ● Pré-condições: Existem pelo menos dois usuários cadastrados no sistema.
   ● Entradas:
     1. Parâmetros: Nenhum.
   ● Passos para Execução:
     1. Acesse a funcionalidade de listagem de usuários.
     2. Solicite a exibição de todos os usuários cadastrados.
     3. Verifique a quantidade de usuários retornados e se as senhas estão ocultas.
   ● Resultado Esperado: O sistema deve exibir a lista completa de usuários cadastrados, sem mostrar as senhas em nenhum dos registros.
   ● Critérios de Sucesso: Todos os usuários cadastrados são exibidos e nenhuma senha aparece nos dados.
   ● Critérios de Falha: A lista está incompleta, vazia ou exibe a senha de algum usuário.

2. Cadastro de Usuário
   ● ID do Caso de Teste: CT-003
   ● Nome: Teste de Cadastro de Usuário
   ● Objetivo: Garantir que um novo usuário seja cadastrado com sucesso, com a senha protegida por criptografia e o perfil correto associado.
   ● Pré-condições: O perfil informado no cadastro existe no sistema.
   ● Entradas:
     1. Nome: "Maria"
     2. E-mail: "maria@example.com"
     3. Senha: "senha1234"
     4. Identificador do Perfil: 1
   ● Passos para Execução:
     1. Acesse a funcionalidade de cadastro de usuários.
     2. Informe os dados do novo usuário (nome, e-mail, senha e perfil).
     3. Confirme o cadastro.
     4. Verifique os dados retornados na confirmação.
   ● Resultado Esperado: O usuário deve ser cadastrado com a senha criptografada e o perfil associado corretamente. A confirmação do cadastro não deve exibir a senha.
   ● Critérios de Sucesso: O usuário é cadastrado, a senha é criptografada e os dados retornados não expõem a senha.
   ● Critérios de Falha: O cadastro não é realizado, a senha é salva sem criptografia ou a senha aparece na confirmação.

3. Atualização de Usuário
   ● ID do Caso de Teste: CT-004
   ● Nome: Teste de Atualização de Usuário
   ● Objetivo: Garantir que as informações básicas de um usuário (nome e e-mail) possam ser atualizadas sem alterar a senha ou o perfil.
   ● Pré-condições: O usuário a ser atualizado existe no sistema.
   ● Entradas:
     1. Identificador do Usuário: 1
     2. Novo Nome: "Novo Nome"
     3. Novo E-mail: "novo@example.com"
   ● Passos para Execução:
     1. Acesse a funcionalidade de atualização de usuários.
     2. Informe o identificador do usuário e os novos dados (nome e e-mail).
     3. Confirme a atualização.
     4. Verifique se o nome e o e-mail foram alterados e se a senha e o perfil permaneceram inalterados.
   ● Resultado Esperado: O nome e o e-mail do usuário devem ser atualizados. A senha e o perfil devem permanecer como estavam antes da alteração.
   ● Critérios de Sucesso: Os dados básicos são alterados com sucesso, sem modificar a senha ou o perfil.
   ● Critérios de Falha: A atualização não é realizada, a senha é alterada ou o perfil é modificado.

4. Atualização de Usuário Inexistente
   ● ID do Caso de Teste: CT-005
   ● Nome: Teste de Atualização de Usuário Inexistente
   ● Objetivo: Garantir que o sistema informe um erro ao tentar atualizar um usuário que não existe.
   ● Pré-condições: O identificador informado não corresponde a nenhum usuário cadastrado.
   ● Entradas:
     1. Identificador do Usuário: 99
     2. Novo Nome: "Novo Nome"
     3. Novo E-mail: "novo@example.com"
   ● Passos para Execução:
     1. Acesse a funcionalidade de atualização de usuários.
     2. Informe um identificador que não existe no sistema, junto com os novos dados.
     3. Tente confirmar a atualização.
     4. Verifique a mensagem de erro exibida pelo sistema.
   ● Resultado Esperado: O sistema deve impedir a atualização e exibir uma mensagem informando que o usuário não foi encontrado.
   ● Critérios de Sucesso: O sistema exibe a mensagem de erro correta e não realiza nenhuma alteração.
   ● Critérios de Falha: O sistema não exibe erro ou tenta realizar a atualização mesmo sem o usuário existir.

Módulo de Cisternas

1. Consulta de Família por ID
   ● ID do Caso de Teste: CT-006
   ● Nome: Teste de Consulta de Família por ID
   ● Objetivo: Garantir que o sistema retorne os dados corretos de uma família ao consultá-la pelo seu identificador.
   ● Pré-condições: A família consultada existe no sistema.
   ● Entradas:
     1. Identificador da Família: 1
   ● Passos para Execução:
     1. Acesse a funcionalidade de consulta de famílias.
     2. Informe o identificador da família desejada.
     3. Verifique os dados retornados pelo sistema.
   ● Resultado Esperado: O sistema deve exibir as informações da família correspondente ao identificador informado.
   ● Critérios de Sucesso: Os dados da família são exibidos corretamente.
   ● Critérios de Falha: O sistema não retorna dados ou exibe informações de outra família.

2. Consulta de Família por ID Inexistente
   ● ID do Caso de Teste: CT-007
   ● Nome: Teste de Consulta de Família por ID Inexistente
   ● Objetivo: Garantir que o sistema informe um erro ao consultar uma família que não existe.
   ● Pré-condições: O identificador informado não corresponde a nenhuma família cadastrada.
   ● Entradas:
     1. Identificador da Família: 99
   ● Passos para Execução:
     1. Acesse a funcionalidade de consulta de famílias.
     2. Informe um identificador que não existe no sistema.
     3. Verifique a mensagem de erro exibida.
   ● Resultado Esperado: O sistema deve exibir uma mensagem informando que a família não foi encontrada.
   ● Critérios de Sucesso: O sistema exibe a mensagem de erro correta.
   ● Critérios de Falha: O sistema não exibe erro ou retorna dados vazios sem aviso.

3. Cadastro de Família com Membros e Cisternas
   ● ID do Caso de Teste: CT-008
   ● Nome: Teste de Cadastro de Família com Membros e Cisternas
   ● Objetivo: Garantir que o sistema cadastre uma família de forma completa, incluindo seus membros e cisternas, e retorne a confirmação dos dados.
   ● Pré-condições: Os dados informados para o cadastro são válidos.
   ● Entradas:
     1. Nome: "Família Souza"
     2. Possui sistema de calhas: Falso
     3. Latitude: -8
     4. Longitude: -36
     5. Status da família: Normal
     6. Membros: Ana, 32 anos, sem deficiência
     7. Cisternas: Capacidade 5000 litros, nível atual 2000 litros
   ● Passos para Execução:
     1. Acesse a funcionalidade de cadastro de famílias.
     2. Informe os dados da família, incluindo os membros e as cisternas.
     3. Confirme o cadastro.
     4. Verifique os dados retornados na confirmação.
   ● Resultado Esperado: A família deve ser cadastrada junto com seus membros e cisternas. O sistema deve retornar a confirmação com o identificador gerado e os detalhes cadastrados.
   ● Critérios de Sucesso: O cadastro é concluído e todos os dados (família, membros e cisternas) são registrados corretamente.
   ● Critérios de Falha: O cadastro não é finalizado ou parte das informações (membros ou cisternas) não é registrada.

4. Atualização de Família com Substituição de Dados
   ● ID do Caso de Teste: CT-009
   ● Nome: Teste de Atualização de Família com Substituição de Dados
   ● Objetivo: Garantir que a atualização de uma família substitua integralmente os dados básicos, os membros e as cisternas pelos novos valores informados.
   ● Pré-condições: A família a ser atualizada existe no sistema.
   ● Entradas:
     1. Identificador da Família: 1
     2. Novo Nome: "Novo Nome"
     3. Possui calha: Falso
     4. Latitude: 1
     5. Longitude: 10
     6. Status: Normal
     7. Membros: Novo, 10 anos, sem deficiência
     8. Cisternas: Capacidade 2000 litros, nível atual 500 litros
   ● Passos para Execução:
     1. Acesse a funcionalidade de atualização de famílias.
     2. Informe o identificador da família e os novos dados completos (incluindo membros e cisternas).
     3. Confirme a atualização.
     4. Verifique se os dados anteriores foram completamente substituídos pelos novos.
   ● Resultado Esperado: Os dados da família, incluindo membros e cisternas, devem ser totalmente substituídos pelas novas informações.
   ● Critérios de Sucesso: Todos os dados antigos são substituídos corretamente pelos novos.
   ● Critérios de Falha: A atualização falha ou os dados antigos são misturados com os novos.

5. Atualização de Família Inexistente
   ● ID do Caso de Teste: CT-010
   ● Nome: Teste de Atualização de Família Inexistente
   ● Objetivo: Garantir que o sistema informe um erro ao tentar atualizar uma família que não existe.
   ● Pré-condições: O identificador informado não corresponde a nenhuma família cadastrada.
   ● Entradas:
     1. Identificador da Família: 404
     2. Novo Nome: "Novo Nome"
     3. Possui calha: Falso
     4. Latitude: 1
     5. Longitude: 10
     6. Status: Normal
     7. Membros: Novo, 10 anos, sem deficiência
     8. Cisternas: Capacidade 2000 litros, nível atual 500 litros
   ● Passos para Execução:
     1. Acesse a funcionalidade de atualização de famílias.
     2. Informe um identificador que não existe no sistema, junto com os novos dados.
     3. Tente confirmar a atualização.
     4. Verifique a mensagem de erro exibida.
   ● Resultado Esperado: O sistema deve impedir a atualização e exibir uma mensagem informando que a família não foi encontrada.
   ● Critérios de Sucesso: O sistema exibe a mensagem de erro correta e não realiza nenhuma alteração.
   ● Critérios de Falha: A atualização é realizada mesmo sem a família existir ou nenhum erro é exibido.

6. Consulta de Detalhes da Família com Autonomia de Água
   ● ID do Caso de Teste: CT-011
   ● Nome: Teste de Consulta de Detalhes da Família com Autonomia de Água
   ● Objetivo: Garantir que a consulta aos detalhes de uma família exiba corretamente o consumo diário de água, a quantidade de dias restantes de autonomia e a data prevista para a próxima entrega.
   ● Pré-condições: A família possui 2 membros e um volume de água armazenado de 150 litros. O consumo do sistema está configurado em 14 litros por pessoa por dia.
   ● Entradas:
     1. Identificador da Família: 1
   ● Passos para Execução:
     1. Acesse a funcionalidade de consulta de detalhes da família.
     2. Informe o identificador da família.
     3. Verifique os valores de consumo diário, dias restantes e data prevista exibidos pelo sistema.
   ● Resultado Esperado: O consumo diário exibido deve ser de 28 litros. A autonomia restante deve ser de 5 dias. A data prevista para a próxima entrega deve corresponder à data atual acrescida de 5 dias.
   ● Critérios de Sucesso: Todos os valores calculados estão corretos.
   ● Critérios de Falha: Algum dos valores calculados apresenta erro.

7. Listagem Paginada de Famílias
   ● ID do Caso de Teste: CT-012
   ● Nome: Teste de Listagem Paginada de Famílias
   ● Objetivo: Garantir que a listagem geral de famílias retorne os registros de forma paginada, respeitando o número de itens por página.
   ● Pré-condições: Existem famílias cadastradas no sistema.
   ● Entradas:
     1. Página desejada: 0
     2. Tamanho da página: 5 registros
   ● Passos para Execução:
     1. Acesse a funcionalidade de listagem de famílias.
     2. Informe a página desejada e a quantidade de registros por página.
     3. Verifique os registros retornados pelo sistema.
   ● Resultado Esperado: O sistema deve exibir a lista de famílias correspondente à página solicitada, respeitando o limite de registros por página.
   ● Critérios de Sucesso: A listagem respeita a paginação configurada.
   ● Critérios de Falha: A paginação não é respeitada ou a listagem não é exibida.

8. Pesquisa Paginada de Famílias por Nome
   ● ID do Caso de Teste: CT-013
   ● Nome: Teste de Pesquisa Paginada de Famílias por Nome
   ● Objetivo: Garantir que a busca de famílias por nome funcione corretamente, retornando apenas as famílias que contenham o termo pesquisado, de forma paginada.
   ● Pré-condições: Existe pelo menos uma família cujo nome contém o termo pesquisado.
   ● Entradas:
     1. Termo de busca: "sil"
     2. Página desejada: 0
     3. Tamanho da página: 5 registros
   ● Passos para Execução:
     1. Acesse a funcionalidade de pesquisa de famílias.
     2. Informe o termo de busca e os parâmetros de paginação.
     3. Verifique os registros retornados pelo sistema.
   ● Resultado Esperado: O sistema deve exibir, de forma paginada, apenas as famílias cujo nome contenha o termo pesquisado.
   ● Critérios de Sucesso: O filtro por nome e a paginação funcionam corretamente.
   ● Critérios de Falha: A pesquisa não encontra resultados válidos ou a paginação não é aplicada.

9. Filtragem Paginada de Famílias por Status
   ● ID do Caso de Teste: CT-014
   ● Nome: Teste de Filtragem Paginada de Famílias por Status
   ● Objetivo: Garantir que o filtro por status (Normal ou Urgente) retorne apenas as famílias com o status selecionado, de forma paginada.
   ● Pré-condições: Existem famílias cadastradas com diferentes status no sistema.
   ● Entradas:
     1. Status de busca: Normal
     2. Página desejada: 0
     3. Tamanho da página: 5 registros
   ● Passos para Execução:
     1. Acesse a funcionalidade de listagem de famílias.
     2. Selecione o filtro de status "Normal" e informe os parâmetros de paginação.
     3. Verifique os registros retornados pelo sistema.
   ● Resultado Esperado: O sistema deve exibir apenas as famílias com o status "Normal", respeitando o limite de registros por página.
   ● Critérios de Sucesso: Somente famílias com o status filtrado são exibidas e a paginação funciona corretamente.
   ● Critérios de Falha: Famílias com outros status aparecem na listagem.

10. Listagem Paginada de Famílias Ordenada por Menor Nível de Cisterna
    ● ID do Caso de Teste: CT-015
    ● Nome: Teste de Listagem Paginada de Famílias Ordenada por Menor Nível de Cisterna
    ● Objetivo: Garantir que a listagem de famílias seja ordenada de forma crescente pelo volume de água armazenado nas cisternas.
    ● Pré-condições: Existem famílias cadastradas com diferentes volumes de água nas cisternas.
    ● Entradas:
      1. Página desejada: 0
      2. Tamanho da página: 5 registros
    ● Passos para Execução:
      1. Acesse a funcionalidade de listagem de famílias.
      2. Selecione a ordenação por menor nível de cisterna.
      3. Verifique a ordem dos registros retornados.
    ● Resultado Esperado: O sistema deve exibir a listagem com a família de menor volume de água em primeiro lugar, seguida pelas demais em ordem crescente.
    ● Critérios de Sucesso: A ordenação crescente por nível de água funciona corretamente.
    ● Critérios de Falha: A listagem é exibida fora de ordem.

11. Listagem Paginada de Famílias Ordenada por Maior Nível de Cisterna
    ● ID do Caso de Teste: CT-016
    ● Nome: Teste de Listagem Paginada de Famílias Ordenada por Maior Nível de Cisterna
    ● Objetivo: Garantir que a listagem de famílias seja ordenada de forma decrescente pelo volume de água armazenado nas cisternas.
    ● Pré-condições: Existem famílias cadastradas com diferentes volumes de água nas cisternas.
    ● Entradas:
      1. Página desejada: 0
      2. Tamanho da página: 5 registros
    ● Passos para Execução:
      1. Acesse a funcionalidade de listagem de famílias.
      2. Selecione a ordenação por maior nível de cisterna.
      3. Verifique a ordem dos registros retornados.
    ● Resultado Esperado: O sistema deve exibir a listagem com a família de maior volume de água em primeiro lugar, seguida pelas demais em ordem decrescente.
    ● Critérios de Sucesso: A ordenação decrescente por nível de água funciona corretamente.
    ● Critérios de Falha: A listagem é exibida fora de ordem.

12. Consumo Diário de Água nas Cisternas
    ● ID do Caso de Teste: CT-017
    ● Nome: Teste de Consumo Diário de Água nas Cisternas
    ● Objetivo: Garantir que a rotina diária do sistema desconte corretamente o nível das cisternas de todas as famílias, com base no número de membros e na taxa de consumo configurada.
    ● Pré-condições: A taxa de consumo do sistema está configurada em 14 litros por pessoa por dia. Existe uma família cadastrada com 2 membros.
    ● Entradas:
      1. Parâmetros: Nenhum.
    ● Passos para Execução:
      1. Acesse ou aguarde a execução da rotina diária de consumo de água.
      2. Verifique o nível das cisternas da família após a execução.
      3. Confirme que a alteração foi registrada no sistema.
    ● Resultado Esperado: O nível de água da cisterna da família deve ser reduzido em 28 litros (14 litros × 2 membros), e a alteração deve ser registrada no sistema.
    ● Critérios de Sucesso: O desconto é calculado corretamente com base no número de membros e aplicado à cisterna.
    ● Critérios de Falha: O nível não é atualizado ou o cálculo do desconto está incorreto.

13. Cálculo de Dias Restantes de Água por Nível e Consumo
    ● ID do Caso de Teste: CT-018
    ● Nome: Teste de Cálculo de Dias Restantes de Água por Nível e Consumo
    ● Objetivo: Garantir que o sistema calcule corretamente a quantidade de dias de autonomia com base no nível atual da cisterna e no consumo diário.
    ● Pré-condições: O sistema está operacional.
    ● Entradas:
      1. Cenário A: Nível: 100 litros | Consumo: 30 litros.
      2. Cenário B: Nível: 500 litros | Consumo: 10 litros.
      3. Cenário C: Nível: 999 litros | Consumo: 100 litros.
    ● Passos para Execução:
      1. Consulte o cálculo de autonomia para cada cenário informado.
      2. Verifique o resultado retornado pelo sistema para cada caso.
    ● Resultado Esperado: Os dias restantes calculados devem ser, respectivamente, 3 dias, 50 dias e 9 dias.
    ● Critérios de Sucesso: O cálculo retorna o número inteiro correto de dias para todos os cenários.
    ● Critérios de Falha: O cálculo retorna valores incorretos ou não arredonda corretamente.

14. Cálculo de Dias Restantes com Consumo Diário Zero
    ● ID do Caso de Teste: CT-019
    ● Nome: Teste de Cálculo de Dias Restantes com Consumo Diário Zero
    ● Objetivo: Garantir que o sistema informe um erro ao tentar calcular a autonomia com consumo diário igual a zero.
    ● Pré-condições: O sistema está operacional.
    ● Entradas:
      1. Nível de água armazenado: 500 litros.
      2. Consumo diário: 0.
    ● Passos para Execução:
      1. Solicite o cálculo de autonomia informando consumo diário igual a zero.
      2. Verifique a mensagem de erro exibida pelo sistema.
    ● Resultado Esperado: O sistema deve impedir o cálculo e exibir uma mensagem informando que o consumo diário deve ser maior que zero.
    ● Critérios de Sucesso: O sistema exibe o erro corretamente e não tenta realizar o cálculo.
    ● Critérios de Falha: O sistema tenta realizar o cálculo e gera uma falha técnica de divisão por zero.

15. Consulta de Configurações do Sistema
    ● ID do Caso de Teste: CT-020
    ● Nome: Teste de Consulta de Configurações do Sistema
    ● Objetivo: Garantir que o sistema exiba os parâmetros globais de configuração quando existirem registros cadastrados.
    ● Pré-condições: As configurações do sistema estão cadastradas.
    ● Entradas:
      1. Parâmetros: Nenhum.
    ● Passos para Execução:
      1. Acesse a funcionalidade de consulta das configurações do sistema.
      2. Verifique os dados exibidos pelo sistema.
    ● Resultado Esperado: O sistema deve exibir os dados de configuração cadastrados.
    ● Critérios de Sucesso: As configurações são exibidas com sucesso.
    ● Critérios de Falha: O sistema não exibe os dados ou apresenta erro.

16. Consulta de Configurações Inexistentes
    ● ID do Caso de Teste: CT-021
    ● Nome: Teste de Consulta de Configurações Inexistentes
    ● Objetivo: Garantir que o sistema informe um erro quando não houver configurações cadastradas.
    ● Pré-condições: Não existem configurações cadastradas no sistema.
    ● Entradas:
      1. Parâmetros: Nenhum.
    ● Passos para Execução:
      1. Acesse a funcionalidade de consulta das configurações do sistema.
      2. Verifique a mensagem de erro exibida.
    ● Resultado Esperado: O sistema deve exibir uma mensagem informando que as configurações não foram encontradas.
    ● Critérios de Sucesso: O sistema identifica a ausência de configurações e exibe a mensagem de erro correta.
    ● Critérios de Falha: O sistema não exibe erro ou retorna dados vazios sem aviso.

17. Atualização de Configurações do Sistema
    ● ID do Caso de Teste: CT-022
    ● Nome: Teste de Atualização de Configurações do Sistema
    ● Objetivo: Garantir que o parâmetro de consumo diário de água possa ser atualizado corretamente no sistema.
    ● Pré-condições: As configurações do sistema já estão cadastradas.
    ● Entradas:
      1. Novo consumo diário de água: 25.5 litros.
    ● Passos para Execução:
      1. Acesse a funcionalidade de atualização das configurações do sistema.
      2. Informe o novo valor de consumo diário de água.
      3. Confirme a atualização.
      4. Verifique se o valor foi alterado corretamente.
    ● Resultado Esperado: O valor de consumo diário deve ser atualizado para 25.5 litros e confirmado pelo sistema.
    ● Critérios de Sucesso: O valor do consumo é atualizado e salvo corretamente.
    ● Critérios de Falha: A atualização falha ou o valor salvo é diferente do informado.

18. Atualização de Configurações Inexistentes
    ● ID do Caso de Teste: CT-023
    ● Nome: Teste de Atualização de Configurações Inexistentes
    ● Objetivo: Garantir que o sistema informe um erro ao tentar atualizar as configurações quando elas não existem.
    ● Pré-condições: Não existem configurações cadastradas no sistema.
    ● Entradas:
      1. Novo consumo diário de água: 25.5 litros.
    ● Passos para Execução:
      1. Acesse a funcionalidade de atualização das configurações do sistema.
      2. Informe o novo valor de consumo diário.
      3. Tente confirmar a atualização.
      4. Verifique a mensagem de erro exibida.
    ● Resultado Esperado: O sistema deve impedir a atualização e exibir uma mensagem informando que as configurações não foram encontradas.
    ● Critérios de Sucesso: O sistema exibe a mensagem de erro correta e não realiza nenhuma alteração.
    ● Critérios de Falha: O sistema não exibe erro ou cria configurações indevidamente.

Módulo de Entregas de Água

1. Registro de Entrega de Água
   ● ID do Caso de Teste: CT-024
   ● Nome: Teste de Registro de Entrega de Água
   ● Objetivo: Garantir que uma entrega de água seja registrada corretamente, atualizando o nível da cisterna da família e criando o histórico da operação.
   ● Pré-condições: A família destinatária possui cisternas com capacidade suficiente para receber a quantidade de água informada.
   ● Entradas:
     1. Data de entrega: Data atual.
     2. Volume solicitado: 1000 litros.
     3. Volume efetivamente entregue: 500 litros.
     4. Identificador da família destinatária: 1.
   ● Passos para Execução:
     1. Acesse a funcionalidade de registro de entregas de água.
     2. Informe a data, os volumes e a família destinatária.
     3. Confirme o registro da entrega.
     4. Verifique se o histórico foi salvo e se o nível da cisterna foi atualizado.
   ● Resultado Esperado: O histórico da entrega deve ser salvo com a data, o volume solicitado e o volume efetivamente entregue. O nível de água da cisterna da família deve ser atualizado com o volume recebido.
   ● Critérios de Sucesso: O registro da entrega é feito com sucesso e o nível da cisterna é atualizado corretamente.
   ● Critérios de Falha: O registro falha ou o nível da cisterna não é atualizado.

2. Registro de Entrega de Água com Desconto de Excedente
   ● ID do Caso de Teste: CT-025
   ● Nome: Teste de Registro de Entrega de Água com Desconto de Excedente
   ● Objetivo: Garantir que, quando a cisterna não comportar todo o volume entregue, o sistema desconte automaticamente a quantidade excedente e registre apenas o volume efetivamente aproveitado.
   ● Pré-condições: A família possui cisternas com capacidade menor do que o volume entregue, resultando em excedente.
   ● Entradas:
     1. Data de entrega: Data atual.
     2. Volume solicitado: 1000 litros.
     3. Volume tentado na entrega: 500 litros.
     4. Identificador da família: 1.
     5. Volume excedente: 100 litros.
   ● Passos para Execução:
     1. Acesse a funcionalidade de registro de entregas de água.
     2. Informe os dados da entrega para uma família cuja cisterna não comporta todo o volume.
     3. Confirme o registro.
     4. Verifique o volume registrado no histórico da entrega.
   ● Resultado Esperado: O sistema deve descontar o volume excedente e registrar no histórico que o volume efetivamente aproveitado pela família foi de 400 litros (500 - 100).
   ● Critérios de Sucesso: O sistema subtrai automaticamente o volume excedente e salva o registro com o valor correto.
   ● Critérios de Falha: O sistema registra o volume total de 500 litros sem descontar o excedente.

3. Consulta de Entregas por Ano e Família
   ● ID do Caso de Teste: CT-026
   ● Nome: Teste de Consulta de Entregas por Ano e Família
   ● Objetivo: Garantir que a consulta ao histórico de entregas retorne os registros filtrados por família e ano.
   ● Pré-condições: Existe pelo menos um registro de entrega para a família e o ano informados.
   ● Entradas:
     1. Ano da pesquisa: 2024.
     2. Identificador da família: 1.
   ● Passos para Execução:
     1. Acesse a funcionalidade de consulta de histórico de entregas.
     2. Informe o ano e o identificador da família.
     3. Verifique os registros retornados pelo sistema.
   ● Resultado Esperado: O sistema deve exibir apenas os registros de entrega da família informada no ano selecionado.
   ● Critérios de Sucesso: A consulta retorna somente os registros que correspondem ao ano e à família informados.
   ● Critérios de Falha: A consulta retorna registros de outros anos ou de outras famílias.

4. Consulta de Entregas por Ano e Família sem Registros
   ● ID do Caso de Teste: CT-027
   ● Nome: Teste de Consulta de Entregas por Ano e Família sem Registros
   ● Objetivo: Garantir que o sistema retorne uma lista vazia, sem erros, quando não houver registros de entrega para a família e o ano consultados.
   ● Pré-condições: A família consultada não possui registros de entregas de água no ano informado.
   ● Entradas:
     1. Ano da pesquisa: 2024.
     2. Identificador da família: 1.
   ● Passos para Execução:
     1. Acesse a funcionalidade de consulta de histórico de entregas.
     2. Informe o ano e o identificador da família.
     3. Verifique o resultado retornado pelo sistema.
   ● Resultado Esperado: O sistema deve retornar uma lista vazia, confirmando que não existem entregas registradas para os critérios informados.
   ● Critérios de Sucesso: A consulta retorna a lista vazia sem apresentar nenhum erro.
   ● Critérios de Falha: O sistema gera um erro devido à ausência de registros ou retorna resultados inválidos.

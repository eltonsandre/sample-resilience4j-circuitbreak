mkcert localhost 127.0.0.1
mkcert -pkcs12 localhost 127.0.0.1 ::1
mkcert -cert-file ./localhost+1.pem, -key-file ./localhost+1-key.pem, -p12-file ./localhost+1.p12 -pkcs12 localhost 127.0.0.1 ::1

# -------- Gerando um Keystore -----------
#Agora vamos criar um conjunto de chaves criptográficas e armazená-lo em um keystore.
#Podemos usar o seguinte comando para gerar nosso formato de keystore PKCS12:
keytool -genkeypair -alias localhost \
  -keyalg RSA -keysize 2048 -storetype PKCS12 \
  -keystore localhost.p12 -validity 3650 \
  -dname "CN=localhost, OU=develop, O=Dev localhost in SSL, L=PPta S=São Paulo C=BR" -ext "san=dns:localhost,ip:127.0.0.1" \
  -storepass changeit -keypass changeit

#Podemos armazenar o maior número de pares de chaves no mesmo keystore, cada um identificado por um alias exclusivo.
#Para gerar nosso keystore em um formato JKS, podemos usar o seguinte comando:
keytool -genkeypair -alias localhost \
  -keyalg RSA -keysize 2048 -keystore localhost.jks \
  -dname "CN=localhost, OU=develop, O=Dev localhost in SSL, L=PPta S=São Paulo C=BR" \
  -ext "san=dns:localhost,ip:127.0.0.1" \
  -validity 3650 -storepass changeit -keypass changeit

#É recomendável usar o formato PKCS12, que é um formato padrão do setor. Portanto, caso já tenhamos um keystore JKS,
# podemos convertê-lo para o formato PKCS12 usando o seguinte comando:
keytool -importkeystore -srckeystore localhost.jks \
  -destkeystore localhost.p12 -deststoretype PKCS12 \
  -storepass changeit -keypass changeit

#remover alias
keytool -delete -alias localhost-ssl -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit

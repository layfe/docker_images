# docker_images

1) Copy the files from "async"
2) Run 'docker build . -t async_test && docker run -p 12345:12345  --rm -it async_test'
3) Check 'curl -v  --cacert ./localhost.crt https://localhost:12345/'  
Should be:  
   < HTTP/2 200  
   < server: nginx/1.18.0  
   < date: Tue, 31 Aug 2021 20:18:42 GMT  
   
4) Run AsyncApacheTest.main()
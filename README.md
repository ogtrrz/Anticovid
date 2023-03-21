# Anticovid
Test Anticovid app

NGINX config

geo@nx-11:/etc/nginx$ cat nginx.conf
user www-data;
worker_processes 4;
pid /run/nginx.pid;
worker_rlimit_nofile 65535;

include /etc/nginx/modules-enabled/*.conf;

events {
worker_connections 768;
# multi_accept on;
}

http {

	##
	# Basic Settings
	##

	sendfile on;
	tcp_nopush on;
	tcp_nodelay on;
	keepalive_timeout 65;
	types_hash_max_size 2048;
	# server_tokens off;

	server_names_hash_bucket_size 64;
	# server_name_in_redirect off;

	include /etc/nginx/mime.types;
	default_type application/octet-stream;

	##
	# SSL Settings
	##

	ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3; # Dropping SSLv3, ref: POODLE
	ssl_prefer_server_ciphers on;

	##
	# Logging Settings
	##

	#access_log /var/log/nginx/access.log;
	#error_log /var/log/nginx/error.log;

	error_log /dev/null emerg;



	##
	# Gzip Settings
	##

	gzip on;

	gzip_vary on;
	gzip_proxied any;
	gzip_comp_level 6;
	gzip_buffers 16 8k;
	#gzip_http_version 2;
	gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;

	##
	# Virtual Host Configs
	##

	include /etc/nginx/conf.d/*.conf;
	include /etc/nginx/sites-enabled/*;
}

==========================================================

geo@nx-11:/etc/nginx$ cat sites-available/nx-11.mexicana.mx
server {

        root /var/www/nx-11.mexicana.mx/html;
        index index.html;

        server_name nx-11.mexicana.mx;

        location / {
		keepalive_timeout 0;
		http2_push img/img.png;
                try_files $uri $uri/ =404;
        }

	location /registrar {
		keepalive_timeout 0;
		proxy_pass http://localhost:8080/registrar;	
	}


#    listen [::]:443 ssl ipv6only=on; # managed by Certbot
    listen 443 ssl http2 reuseport; # managed by Certbot
    ssl_certificate /etc/letsencrypt/live/nx-11.mexicana.mx/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/nx-11.mexicana.mx/privkey.pem; # managed by Certbot
    include /etc/letsencrypt/options-ssl-nginx.conf; # managed by Certbot
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem; # managed by Certbot



    # security
    include                 nginxconfig.io/security.conf;


    if ($request_method !~ ^(GET|POST|HEAD|CONNECT)$) {
	return '405';
    }


}
server {
if ($host = nx-11.mexicana.mx) {
return 301 https://$host$request_uri;
} # managed by Certbot


        listen 80;
        #listen [::]:80;

        server_name nx-11.mexicana.mx;
    return 404; # managed by Certbot


}

===============================================

Host characteristics
Google Cloud n1-standard-4

geo@nx-11:~$ lscpu
Architecture:                    x86_64
CPU op-mode(s):                  32-bit, 64-bit
Byte Order:                      Little Endian
Address sizes:                   46 bits physical, 48 bits virtual
CPU(s):                          4
On-line CPU(s) list:             0-3
Thread(s) per core:              2
Core(s) per socket:              2
Socket(s):                       1
NUMA node(s):                    1
Vendor ID:                       GenuineIntel
CPU family:                      6
Model:                           63
Model name:                      Intel(R) Xeon(R) CPU @ 2.30GHz
Stepping:                        0
CPU MHz:                         2299.998
BogoMIPS:                        4599.99
Hypervisor vendor:               KVM
Virtualization type:             full
L1d cache:                       64 KiB
L1i cache:                       64 KiB
L2 cache:                        512 KiB
L3 cache:                        45 MiB
NUMA node0 CPU(s):               0-3
Vulnerability Itlb multihit:     Not affected
Vulnerability L1tf:              Mitigation; PTE Inversion
Vulnerability Mds:               Mitigation; Clear CPU buffers; SMT Host state unknown
Vulnerability Meltdown:          Mitigation; PTI
Vulnerability Mmio stale data:   Vulnerable: Clear CPU buffers attempted, no microcode; SMT Host state unknown
Vulnerability Retbleed:          Mitigation; IBRS
Vulnerability Spec store bypass: Mitigation; Speculative Store Bypass disabled via prctl and seccomp
Vulnerability Spectre v1:        Mitigation; usercopy/swapgs barriers and __user pointer sanitization
Vulnerability Spectre v2:        Mitigation; IBRS, IBPB conditional, RSB filling, PBRSB-eIBRS Not affected
Vulnerability Srbds:             Not affected
Vulnerability Tsx async abort:   Not affected
Flags:                           fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush mmx fxsr sse sse2 ss ht syscall nx pdpe1gb rdtscp lm constant_tsc rep_good nopl xtopology n
onstop_tsc cpuid tsc_known_freq pni pclmulqdq ssse3 fma cx16 pcid sse4_1 sse4_2 x2apic movbe popcnt aes xsave avx f16c rdrand hypervisor lahf_lm abm invpcid_single pti ssb
d ibrs ibpb stibp fsgsbase tsc_adjust bmi1 avx2 smep bmi2 erms invpcid xsaveopt arat md_clear arch_capabilities

============================================

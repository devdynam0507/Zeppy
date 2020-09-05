# Simple Cryptocurrency 'Zeppy'

거래, 블록체인, 지갑 기능만 있는 심플한 암호화폐입니다.

# Zeppy Network
Decentralized P2P로 DNS에서 풀 노드의 주소를 받아오고 <br>
풀노드는 자신이 연결된 피어들 5개를 반환하여 피어들과 연결합니다.  
Message Server: 동작과 관련된 OSGI역활을 합니다.  
FileTransferServer: 파일 전송과 관련된 프로토콜입니다.  
DNSClient: DNS와 연결하여 풀노드 주소를 받아옵니다.

모든 패킷 ID는 1byte로 이루어져 있습니다.

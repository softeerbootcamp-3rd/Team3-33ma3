// 현재 위치 좌표 반환 함수
function getCurrentLocation() {
  return new Promise((resolve, reject) => {
    if (!navigator.geolocation) {
      reject("위치 권한을 허용해주세요.");
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        resolve({ latitude, longitude });
      },
      (error) => {
        reject(error);
      }
    );
  });
}

function searchAddressToCoordinate(address) {
  return new Promise((resolve, reject) => {
    naver.maps.Service.geocode(
      {
        query: address ? address : "DEFAULT",
      },
      function (status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
          return reject("위치 불러오기 실패");
        }

        if (response.v2.meta.totalCount === 0) {
          // 주소 검색 결과가 없을 경우
          return resolve(null);
        }

        const item = response.v2.addresses[0]; // 찾은 주소 정보
        const point = new naver.maps.Point(Number(item.x), Number(item.y)); // 지도에서 이동할 좌표
        resolve({ address: item, point: point });
      }
    );
  });
}

function searchCoordinateToAddress(latLng) {
  return new Promise((resolve, reject) => {
    naver.maps.Service.reverseGeocode(
      {
        coords: latLng,
        orders: [
          naver.maps.Service.OrderType.ADDR,
          naver.maps.Service.OrderType.ROAD_ADDR,
        ].join(","),
      },
      function (status, response) {
        if (status !== naver.maps.Service.Status.OK) {
          return reject("위치 불러오기 실패");
        }
        const address = response.v2.address.roadAddress
          ? response.v2.address.roadAddress
          : response.v2.address.jibunAddress;
        resolve(address);
      }
    );
  });
}

export {
  getCurrentLocation,
  searchAddressToCoordinate,
  searchCoordinateToAddress,
};

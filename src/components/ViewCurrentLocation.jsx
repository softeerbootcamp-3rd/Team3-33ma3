import { useEffect } from "react";

export default function ViewCurrentLocation() {
  useEffect(() => {
    const mapOptions = {
      center: new N.LatLng(37.3595704, 127.105399),
      zoom: 12,
      scaleControl: false,
    };

    const map = new N.Map("map", mapOptions);

    var markerOptions = {
      position: map.getCenter(),
      map: map,
    };

    var marker = new naver.maps.Marker(markerOptions);

    N.Event.addListener(map, "dragend", function (e) {
      console.log("drag end");
    });
  }, []);

  return (
    <div style={{ position: "relative", width: "400px", height: "400px" }}>
      <div id="map" style={{ width: "100%", height: "100%" }} />
      <div
        style={{
          position: "absolute",
          left: "50%",
          top: "50%",
          transform: "translate(-50%, -50%)",
        }}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="25"
          height="25"
          viewBox="0 0 35 36"
          fill="none"
        >
          <path
            d="M10.5 27.0718L8.75 25.3218L15.75 18.3218L8.75 11.3218L10.5 9.57178L17.5 16.5718L24.5 9.57178L26.25 11.3218L19.25 18.3218L26.25 25.3218L24.5 27.0718L17.5 20.0718L10.5 27.0718Z"
            fill="#6E7191"
          />
        </svg>
      </div>
    </div>
  );
}

// window.navermap_authFailure = function () {
//   // 인증 실패 시 처리 코드 작성
//   // ...
// };

/*
map.setCenter(jeju); // 중심 좌표 이동
map.setZoom(13);     // 줌 레벨 변경

var seoul = new naver.maps.LatLngBounds(
    new naver.maps.LatLng(37.42829747263545, 126.76620435615891),
    new naver.maps.LatLng(37.7010174173061, 127.18379493229875));

map.fitBounds(seoul); // 좌표 경계 이동

이벤트 리스너를 등록 해제하면 더 이상 DOM 이벤트를 수신하지 않습니다. removeDOMListener 메서드를 이용해 등록한 리스너를 제거합니다.
이때, 인수로 DOMEventListener 객체(또는 객체 배열)를 전달할 수 있습니다.

다음 예제는 지도를 클릭했을 때, 마커가 있는 위치로 이동하며 줌 레벨을 변경하는 예제입니다.

var mapOptions = {
    zoom: 4,
    center: new naver.maps.LatLng(37.3614483, 127.1114883)
};

var map = new naver.maps.Map('map', mapOptions);

var markerOptions = {
    position: map.getCenter(),
    map: map,
    title: 'Click to zoom'
};

var marker = new naver.maps.Marker(markerOptions);

var mapElement = map.getElement();

var listener = naver.maps.Event.addDOMListener(mapElement, 'click', function() {
    map.setZoom(8);
    map.setCenter(marker.getPosition());

    naver.maps.Event.removeDOMListener(listener);
});

var markerOptions = {
    position: map.getCenter(),
    map: map,
  };

  var marker = new naver.maps.Marker(markerOptions);
  const listener = N.Event.addListener(map, "click", function (e) {
    map.setZoom(8);
    map.setCenter(marker.getPosition());
    naver.maps.Event.removeListener(listener);
  });
*/

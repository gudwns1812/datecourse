import LuckyDraw from "@/components/features/LuckyDraw";
import StationCard from "@/components/features/StationCard";

export default function Home() {
  return (
    <div className="flex-1 flex flex-col">
      {/* Hero Section */}
      <section className="relative flex-1 flex flex-col items-center justify-center px-6 py-20 overflow-hidden">
        {/* Background Decoration */}
        <div className="absolute inset-0 z-0 opacity-40 pointer-events-none">
          <div className="absolute inset-0 bg-gradient-to-b from-transparent via-background-light/50 to-background-light dark:via-background-dark/50 dark:to-background-dark"></div>
          <svg className="absolute w-full h-full" viewBox="0 0 1000 1000" xmlns="http://www.w3.org/2000/svg">
            <path d="M0,200 Q250,150 500,200 T1000,150" fill="none" stroke="#ee2b8c" strokeOpacity="0.3" strokeWidth="2"></path>
            <path d="M200,0 Q250,400 200,1000" fill="none" stroke="#ee2b8c" strokeOpacity="0.2" strokeWidth="2"></path>
            <circle cx="500" cy="450" r="300" fill="none" stroke="#ee2b8c" strokeDasharray="10 10" strokeOpacity="0.2" strokeWidth="1"></circle>
          </svg>
        </div>

        <div className="z-10 text-center max-w-3xl w-full">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 text-primary font-bold text-sm mb-6 border border-primary/20">
            <span className="material-symbols-outlined text-sm">favorite</span>
            <span>오늘은 특별한 곳에서 만나요</span>
          </div>
          
          <h2 className="text-4xl md:text-6xl font-extrabold leading-tight mb-6 bg-clip-text text-transparent bg-gradient-to-r from-primary to-pink-400">
            오늘 우리 어디서 만날까?
          </h2>
          
          <p className="text-lg md:text-xl text-slate-600 dark:text-slate-400 mb-12 max-w-2xl mx-auto">
            결정 장애 커플을 위한 최고의 해결책!<br className="hidden md:block"/>
            서울의 300여 개 지하철역 중 랜덤으로 데이트 장소를 추천해드려요.
          </p>

          <LuckyDraw />
        </div>
      </section>

      {/* Features/Stats Section */}
      <section className="max-w-7xl mx-auto w-full px-6 py-20 grid grid-cols-1 md:grid-cols-3 gap-8 border-t border-slate-100 dark:border-slate-800">
        <div className="p-8 rounded-3xl bg-white dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700 hover:border-primary/50 transition-colors shadow-sm">
          <div className="w-12 h-12 rounded-xl bg-primary/10 text-primary flex items-center justify-center mb-6">
            <span className="material-symbols-outlined">restaurant</span>
          </div>
          <h3 className="text-xl font-bold mb-3">주변 맛집 추천</h3>
          <p className="text-slate-500 dark:text-slate-400">역을 뽑으면 해당 역 주변의 엄선된 데이트 맛집 정보가 함께 제공됩니다.</p>
        </div>
        
        <div className="p-8 rounded-3xl bg-white dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700 hover:border-primary/50 transition-colors shadow-sm">
          <div className="w-12 h-12 rounded-xl bg-blue-500/10 text-blue-500 flex items-center justify-center mb-6">
            <span className="material-symbols-outlined">map</span>
          </div>
          <h3 className="text-xl font-bold mb-3">상세 데이트 코스</h3>
          <p className="text-slate-500 dark:text-slate-400">식사부터 카페, 산책로까지 완벽한 동선의 데이트 코스를 짜드려요.</p>
        </div>

        <div className="p-8 rounded-3xl bg-white dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700 hover:border-primary/50 transition-colors shadow-sm">
          <div className="w-12 h-12 rounded-xl bg-orange-500/10 text-orange-500 flex items-center justify-center mb-6">
            <span className="material-symbols-outlined">star</span>
          </div>
          <h3 className="text-xl font-bold mb-3">나만의 위시리스트</h3>
          <p className="text-slate-500 dark:text-slate-400">마음에 드는 역과 장소는 저장해두고 언제든 꺼내보세요.</p>
        </div>
      </section>
    </div>
  );
}

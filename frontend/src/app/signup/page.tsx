"use client";

import { Suspense, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import Button from "@/components/common/Button";
import Input from "@/components/common/Input";
import { authService } from "@/services/auth";
import { useAuthStore } from "@/store/useAuthStore";
function SignupForm() {
    const router = useRouter();

    const [formData, setFormData] = useState({
        username: "",
        loginId: "",
        password: "",
        email: "",
        birth: "",
        gender: "MALE" as "MALE" | "FEMALE",
        phoneNumber: "",
    });

    const [loginType, setLoginType] = useState<string | null>(null);

    // 확인 후 처리
    const [idStatus, setIdStatus] = useState<'idle' | 'checking' | 'available' | 'taken'>('idle');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const isLoggedIn = useAuthStore((state) => state.isLoggedIn);
    const authChecked = useAuthStore((state) => state.authChecked);

    useEffect(() => {
        if (authChecked && isLoggedIn) {
            router.replace("/");
        }
    }, [authChecked, isLoggedIn, router]);

    useEffect(() => {
        // 쿠키에서 loginType 읽기
        const cookies = document.cookie.split('; ');
        const loginTypeCookie = cookies.find(row => row.startsWith('loginType='));

        if (loginTypeCookie) {
            const type = loginTypeCookie.split('=')[1];
            setLoginType(type);

            // 읽은 후 즉시 쿠키 삭제 (중복 처리 방지 및 보안)
            document.cookie = "loginType=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        }
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));

        // 아이디 필드가 변경되면 중복 확인 상태 초기화
        if (name === 'loginId') {
            setIdStatus('idle');
        }
    };


    const handleCheckId = async () => {
        if (!formData.loginId) {
            alert("아이디를 먼저 입력해주세요.");
            return;
        }

        setIdStatus('checking');
        try {
            const response = await authService.checkId(formData.loginId);
            if (response.result === "SUCCESS") {
                // data가 true면 사용 가능, false면 중복됨
                setIdStatus(response.data ? 'taken' : 'available');
            } else {
                setIdStatus('idle');
                setError("중복 확인 중 오류가 발생했습니다.");
            }
        } catch {
            setIdStatus('idle');
            setError("서버와의 통신에 실패했습니다.");
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (idStatus !== 'available') {
            alert("아이디 중복 확인이 필요합니다.");
            return;
        }

        setError(null);
        setIsLoading(true);

        try {
            const response = await authService.signup(formData);
            if (response.result === "SUCCESS") {
                alert("회원가입이 완료되었습니다. 로그인해주세요!");
                router.push("/login");
            } else {
                setError("회원가입 정보가 올바르지 않습니다.");
            }
        } catch {
            setError("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="flex-1 flex items-center justify-center px-6 py-12">
            <div
                className="w-full max-w-lg bg-white dark:bg-slate-900 rounded-3xl p-8 md:p-10 shadow-2xl border border-primary/5">
                <div className="text-center mb-10">
                    <div
                        className="inline-flex items-center justify-center p-3 bg-primary/10 rounded-2xl text-primary mb-4">
                        <span className="material-symbols-outlined text-3xl">person_add</span>
                    </div>
                    <h1 className="text-3xl font-extrabold text-slate-900 dark:text-slate-100 mb-2">
                        {loginType ? "추가 정보 입력" : "회원가입"}
                    </h1>
                    <p className="text-slate-500 dark:text-slate-400">
                        {loginType
                            ? "카카오 계정 연동을 위해 몇 가지 정보가 더 필요해요."
                            : "dateCourse의 회원이 되어 서울 곳곳을 탐험하세요."}
                    </p>
                </div>

                {loginType && (
                    <div
                        className="mb-8 flex items-center justify-center gap-2 py-2 px-4 bg-[#FEE500]/10 rounded-full border border-[#FEE500]/20 w-fit mx-auto">
                        <div className="w-5 h-5 bg-[#FEE500] rounded-full flex items-center justify-center">
                            <svg width="12" height="12" viewBox="0 0 24 24" fill="none"
                                xmlns="http://www.w3.org/2000/svg">
                                <path fillRule="evenodd" clipRule="evenodd"
                                    d="M12 3C6.477 3 2 6.48 2 10.8c0 2.8 1.88 5.26 4.7 6.64-.18.64-.66 2.32-.76 2.67-.12.4.12.4.26.32.1.06 1.62-1.1 2.28-1.55.5.14 1 .22 1.52.22 5.523 0 10-3.48 10-7.8S17.523 3 12 3z"
                                    fill="#191919" />
                            </svg>
                        </div>
                        <span
                            className="text-xs font-bold text-slate-600 dark:text-slate-300">{loginType === 'kakao' ? '카카오' : loginType} 계정이 연동되었습니다</span>
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-5">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                        <div className="space-y-2">
                            <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">이름</label>
                            <Input
                                name="username"
                                placeholder="홍길동"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="space-y-2">
                            <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">아이디</label>
                            <div className="flex gap-2">
                                <Input
                                    name="loginId"
                                    placeholder="사용할 아이디"
                                    value={formData.loginId}
                                    onChange={handleChange}
                                    required
                                    className="flex-1"
                                />
                                <button
                                    type="button"
                                    onClick={handleCheckId}
                                    disabled={idStatus === 'checking' || !formData.loginId}
                                    className="px-4 py-2 bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-300 text-sm font-bold rounded-xl transition-all disabled:opacity-50 whitespace-nowrap"
                                >
                                    {idStatus === 'checking' ? '확인 중...' : '중복 확인'}
                                </button>
                            </div>
                            {idStatus === 'available' && (
                                <p className="text-xs font-bold text-green-500 ml-1 italic animate-in fade-in slide-in-from-left-1">사용
                                    가능한 아이디입니다.</p>
                            )}
                            {idStatus === 'taken' && (
                                <p className="text-xs font-bold text-primary ml-1 italic animate-in fade-in slide-in-from-left-1">이미
                                    사용 중인 아이디입니다.</p>
                            )}
                        </div>
                    </div>

                    <div className="space-y-2">
                        <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">비밀번호</label>
                        <Input
                            name="password"
                            type="password"
                            placeholder="비밀번호를 입력하세요"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            fullWidth
                        />
                    </div>

                    <div className="space-y-2">
                        <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">이메일</label>
                        <Input
                            name="email"
                            type="email"
                            placeholder="test@example.com"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            fullWidth
                        />
                    </div>

                    <div className="space-y-2">
                        <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">생년월일</label>
                        <Input
                            name="birth"
                            type="date"
                            value={formData.birth}
                            onChange={handleChange}
                            required
                            fullWidth
                        />
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                        <div className="space-y-2">
                            <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">성별</label>
                            <div className="flex gap-4">
                                <label
                                    className="flex-1 flex items-center justify-center gap-2 p-3 bg-primary/5 rounded-xl border-2 border-transparent has-[:checked]:border-primary has-[:checked]:bg-primary/10 cursor-pointer transition-all">
                                    <input
                                        type="radio"
                                        name="gender"
                                        value="MALE"
                                        checked={formData.gender === "MALE"}
                                        onChange={handleChange}
                                        className="hidden"
                                    />
                                    <span className="font-bold text-slate-700 dark:text-slate-300">남성</span>
                                </label>
                                <label
                                    className="flex-1 flex items-center justify-center gap-2 p-3 bg-primary/5 rounded-xl border-2 border-transparent has-[:checked]:border-primary has-[:checked]:bg-primary/10 cursor-pointer transition-all">
                                    <input
                                        type="radio"
                                        name="gender"
                                        value="FEMALE"
                                        checked={formData.gender === "FEMALE"}
                                        onChange={handleChange}
                                        className="hidden"
                                    />
                                    <span className="font-bold text-slate-700 dark:text-slate-300">여성</span>
                                </label>
                            </div>
                        </div>
                        <div className="space-y-2">
                            <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">전화번호</label>
                            <Input
                                name="phoneNumber"
                                placeholder="010-1234-5678"
                                value={formData.phoneNumber}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    {error && (
                        <p className="text-sm font-bold text-primary text-center bg-primary/5 py-3 rounded-xl border border-primary/10 mt-4">
                            {error}
                        </p>
                    )}

                    <Button
                        type="submit"
                        size="lg"
                        className="w-full mt-6"
                        disabled={isLoading || idStatus !== 'available'}
                    >
                        {isLoading ? "처리 중..." : "가입하기"}
                    </Button>
                </form>

                <div className="mt-8 text-center text-sm border-t border-slate-100 dark:border-slate-800 pt-8">
                    <p className="text-slate-500 dark:text-slate-400">
                        이미 계정이 있으신가요?{" "}
                        <Link href="/login" className="text-primary font-bold hover:underline">
                            로그인하기
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default function SignupPage() {
    return (
        <Suspense fallback={
            <div className="flex-1 flex items-center justify-center">
                <div className="animate-pulse text-primary font-bold text-lg">페이지를 불러오는 중...</div>
            </div>
        }>
            <SignupForm />
        </Suspense>
    );
}
